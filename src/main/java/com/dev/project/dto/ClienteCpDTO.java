package com.dev.project.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteCpDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	@Length(min=8, message="A senha deve conter ao menos 8 caracteres.")
	@NotEmpty(message="Preenchimento obrigatório.")
	private String senha;
	
	public ClienteCpDTO() {
		
	}
	
	public ClienteCpDTO(Integer id, String senha) {
		this.id = id;
		this.senha = senha;
	}
}