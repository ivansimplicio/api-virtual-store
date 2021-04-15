package com.dev.project.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cidade implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;
	@Column(nullable = false)
	private String nome;

	@ManyToOne
	@JoinColumn(name="estado_id")
	private Estado estado;
	
	public Cidade() {
	}
	
	public Cidade(Integer id, String nome, Estado estado) {
		this.id = id;
		this.nome = nome;
		this.estado = estado;
	}	
}