package com.dev.project.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.dev.project.domain.Categoria;
import com.dev.project.domain.Produto;
import com.dev.project.dto.ProdutoNewDTO;
import com.dev.project.repositories.CategoriaRepository;
import com.dev.project.repositories.ProdutoRepository;
import com.dev.project.services.exceptions.DataIntegrityException;
import com.dev.project.services.exceptions.ObjectNotFoundException;

@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoRepository repo;
	
	@Autowired
	private CategoriaService categoriaService;
	
	@Autowired
	private CategoriaRepository categoriaRepository;

	public Produto find(Integer id) {
		Optional<Produto> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: "+id
																+", Tipo: "+Produto.class.getName()));
	}

	public Produto insert(Produto obj) {
		List<Categoria> categorias = new ArrayList<>();
		for(Categoria c : obj.getCategorias()) {
			Categoria aux = categoriaService.find(c.getId());
			aux.getProdutos().add(obj);
			categorias.add(aux);
		}
		obj.setCategorias(categorias);
		try {
			obj = repo.save(obj);
		}catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Produto já cadastrado!");
		}
		categoriaRepository.saveAll(categorias);
		return obj;
	}
	
	public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		List<Categoria> categorias = categoriaRepository.findAllById(ids);
		return repo.findDistinctByNomeContainingAndCategoriasIn(nome, categorias, pageRequest);
	}

	public Produto fromDTO(ProdutoNewDTO objDTO) {
		Produto obj = new Produto(null, objDTO.getNome(), objDTO.getPreco());
		for(Integer idCat : objDTO.getCategorias()) {
			obj.getCategorias().add(new Categoria(idCat, null));
		}
		return obj;
	}
}