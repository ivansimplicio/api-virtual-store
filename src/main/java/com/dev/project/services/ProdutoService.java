package com.dev.project.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
	
	@Value("${img.prefix.product.image}")
	private String prefix;
	
	@Value("${img.suffix.product.image}")
	private String suffix;
	
	@Value("${img.profile.size}")
	private Integer sizeThumb;
	
	@Value("${img.size.standard}")
	private Integer sizeStandard;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private S3Service s3Service;

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

	public URI uploadProfilePicture(Integer id, MultipartFile multipartFile) {
		find(id);
		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
		
		BufferedImage thumbImage = imageService.cropSquare(jpgImage);
		thumbImage = imageService.resize(jpgImage, sizeThumb);
		
		BufferedImage standardImage = imageService.cropSquare(jpgImage);
		standardImage = imageService.resize(jpgImage, sizeStandard);
		
		String fileNameThumbImage = prefix + id + suffix + ".jpg";
		String fileNameStandardImage = prefix + id + ".jpg";
		
		s3Service.uploadFile(imageService.getInputStream(thumbImage, "jpg"), fileNameThumbImage, "image");
		return s3Service.uploadFile(imageService.getInputStream(standardImage, "jpg"), fileNameStandardImage, "image");
	}
}