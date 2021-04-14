package com.dev.project.dto;

import java.io.Serializable;

import com.dev.project.domain.Cidade;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CidadeDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String nome;
	
	public CidadeDTO() {
		
	}
	
	public CidadeDTO(Cidade obj) {
		setId(obj.getId());
		setNome(obj.getNome());
	}
}