package com.dev.project.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.project.domain.Cliente;
import com.dev.project.domain.Endereco;
import com.dev.project.domain.ItemPedido;
import com.dev.project.domain.PagamentoComBoleto;
import com.dev.project.domain.Pedido;
import com.dev.project.domain.enums.EstadoPagamento;
import com.dev.project.domain.enums.Perfil;
import com.dev.project.repositories.ItemPedidoRepository;
import com.dev.project.repositories.PagamentoRepository;
import com.dev.project.repositories.PedidoRepository;
import com.dev.project.security.UserSS;
import com.dev.project.services.exceptions.AuthorizationException;
import com.dev.project.services.exceptions.DataIntegrityException;
import com.dev.project.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;

	public Pedido find(Integer id) {
		
		UserSS user = UserService.authenticated();
		
		if(user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado!");
		}
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: "+id
																+", Tipo: "+Pedido.class.getName()));
	}
	
	private boolean containsEndereco(Cliente cliente, Integer idEndereco) {
		for(Endereco e : cliente.getEnderecos()) {
			if(e.getId() == idEndereco) {
				return true;
			}
		}
		return false;
	}
	
	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		Cliente cliente = clienteService.find(obj.getCliente().getId());
		obj.setCliente(cliente);
		int idEndereco = obj.getEnderecoDeEntrega().getId();
		if(!containsEndereco(cliente, idEndereco)) {
			throw new DataIntegrityException("O endereço informado não pertence ao cliente!");
		}
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		
		for(ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		itemPedidoRepository.saveAll(obj.getItens());
		emailService.sendOrderConfirmationHtmlEmail(obj);
		return obj;
	}
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		UserSS user = UserService.authenticated();
		if(user == null) {
			throw new AuthorizationException("Acesso negado!");
		}
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente = clienteService.find(user.getId());
		return repo.findByCliente(cliente, pageRequest);
	}
}