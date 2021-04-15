package com.dev.project.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoNewDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	@NotEmpty(message="Preenchimento obrigatório.")
	@Length(min=5, max=80, message="O tamanho deve ser entre 5 e 80 caracteres.")
	private String nome;
	private Double preco;
	@NotEmpty(message="Preenchimento obrigatório.")
	private List<Integer> categorias = new ArrayList<>();
	
	public ProdutoNewDTO() {
		
	}
	
	public ProdutoNewDTO(Integer id, String nome, Double preco) {
		this.id = id;
		this.nome = nome;
		this.preco = preco;
	}
}