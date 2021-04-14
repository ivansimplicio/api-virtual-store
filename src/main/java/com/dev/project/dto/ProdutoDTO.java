package com.dev.project.dto;

import java.io.Serializable;

import com.dev.project.domain.Produto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String nome;
	private Double preco;
	
	public ProdutoDTO() {
	}
	
	public ProdutoDTO(Produto produto) {
		setId(produto.getId());
		setNome(produto.getNome());
		setPreco(produto.getPreco());
	}
}