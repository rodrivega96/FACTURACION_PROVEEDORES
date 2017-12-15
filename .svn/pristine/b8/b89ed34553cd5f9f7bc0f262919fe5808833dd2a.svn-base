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
@Table(name = "OC_FACTURA@PREFA")
@XmlRootElement(name = "facturaCliente")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true)
@NoArgsConstructor
@Getter
@Setter
public class FacturaCliente implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7660238347809235022L;

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "CLIENTE_ID")
	private Long clienteId;

	@Column(name = "NUMERO")
	private String numero;
	
	@Column(name = "TOTAL")
	private Double total;
	
	@Column(name = "NETO")
	private Double neto;
	

}
