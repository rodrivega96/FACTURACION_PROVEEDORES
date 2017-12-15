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
@Table(name = "OC_FACTURA_OC")
@XmlRootElement(name = "facturaClienteOC")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true)
@NoArgsConstructor
@Getter
@Setter
public class FacturaClienteOC implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3835680811148497757L;

	@Id
	@Column(name = "OC_ID")
	private Long idOrden;

	@Column(name = "FACTURA_NRO")
	private String facturaNro;

}
