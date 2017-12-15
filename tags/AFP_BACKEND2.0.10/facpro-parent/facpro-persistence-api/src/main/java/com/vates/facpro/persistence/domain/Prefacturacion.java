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
@Table(name = "OC_PREFACTURACION@PREFA")
@XmlRootElement(name = "prefacturacion")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true)
@NoArgsConstructor
@Getter
@Setter
public class Prefacturacion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 220866578553501090L;

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "PROYECTO_ID")
	private Long proyectoId;
	
	@Column(name = "ESTADO_ID")
	private Long estadoId;
	
	@Column(name = "CLIENTE_ID")
	private Long clienteId;
	
	@Column(name = "DESCRIPCION")
	private String descripcion;
	
	@Column(name = "PM")
	private String pm;
	
	@Column(name = "PM_ID")
	private Long pmId;
	
	@Column(name = "SALDO")
	private Double saldo;
	
	@Column(name = "PERIODO")
	private Date periodo;
	
	@Column(name = "FACTURA")
	private Long factura;
	
	@Column(name = "PROYECTO_NOMBRE")
	private String proyectoNombre;
	
	@Column(name = "CLIENTE_NOMBRE")
	private String clienteNombre;
	
	@Column(name = "ESTADO_NOMBRE")
	private String estadoNombre;

}
