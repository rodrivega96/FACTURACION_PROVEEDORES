package com.vates.facpro.persistence.domain;

import java.io.Serializable;
import java.util.Date;

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
@Table(name = "OC_PROPUESTA@PREFA")
@XmlRootElement(name = "propuesta")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true)
@NoArgsConstructor
@Getter
@Setter
public class Propuesta implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -3108446446444155015L;

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "COMERCIAL")
	private String comercial;

	@Column(name = "DESCRIPCION")
	private String descripcion;
	
	@Column(name = "VIGENTE_DESDE")
	private Date vigenteDesde;
	
	@Column(name = "VIGENTE_HASTA")
	private Date vigenteHasta;
	
	@Column(name = "CLIENTE_ID")
	private Long clienteId;
	

}
