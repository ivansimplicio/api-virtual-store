package com.dev.project.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.dev.project.domain.Cliente;
import com.dev.project.services.validation.ClienteUpdate;

import lombok.Getter;
import lombok.Setter;

@ClienteUpdate
@Getter
@Setter
public class ClienteDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	@NotEmpty(message="Preenchimento obrigatório.")
	@Length(min=5, max=120, message="O tamanho deve ser entre 5 e 120 caracteres.")
	private String nome;
	@NotEmpty(message="Preenchimento obrigatório.")
	@Email(message="Email inválido.")
	private String email;
	
	public ClienteDTO() {
	}
	
	public ClienteDTO(Cliente obj) {
		setId(obj.getId());
		setNome(obj.getNome());
		setEmail(obj.getEmail());
	}
}