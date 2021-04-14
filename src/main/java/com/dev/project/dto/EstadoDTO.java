package com.dev.project.dto;

import java.io.Serializable;

import com.dev.project.domain.Estado;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstadoDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String nome;
	
	public EstadoDTO() {
		
	}
	
	public EstadoDTO(Estado obj) {
		setId(obj.getId());
		setNome(obj.getNome());
	}
}