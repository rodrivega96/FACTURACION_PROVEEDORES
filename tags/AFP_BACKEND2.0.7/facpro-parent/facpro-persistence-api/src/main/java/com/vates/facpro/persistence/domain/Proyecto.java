package com.vates.facpro.persistence.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * 
 * @author Cabrera Manuel
 * 
 */
@Entity
@Table(name = "OC_PROYECTO@PREFA")
@XmlRootElement(name = "proyecto")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true)
@NoArgsConstructor
@Getter
@Setter
public class Proyecto implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7310547882014395439L;

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "NOMBRE")
	private String nombre;
	
	@Column(name = "PM_ID")
	private Long pmId;

	@Column(name = "PM")
	private String pm;
	
	@Column(name = "PMG_ID")
	private Long pmgId;

	@Column(name = "PMG")
	private String pmg;
	
	@Column(name = "ACTIVO")
	private Integer activo;
	
	@Column(name = "CLIENTE_ID")
	private Long clienteId;

}
