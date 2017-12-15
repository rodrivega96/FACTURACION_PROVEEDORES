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
 * Entity representing a system's user.
 * 
 * @author Gaston Napoli
 * 
 */
@Entity
@Table(name = "VIS_CONTROL_GASTO@FACTURACION")
@XmlRootElement(name = "facturaAsientoAdm")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true)
@NoArgsConstructor
@Getter
@Setter
public class FacturaAsientoAdm implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2459236157618194607L;

	public static int A_VENCER = 3;
	public static Long SIN_CENTRO = 0L;

	@Id
	@Column(name = "ID")
	private Long id;

	@Id
	@Column(name = "NRO_CUENTA")
	private Long nroCuenta;

	@Column(name = "DESCRIPCION_CUENTA")
	private String descripcionCuenta;

	@Id
	@Column(name = "NRO_CTRO_CTOS")
	private Long nroCentroCostos;

	@Column(name = "DESCRIPCION_CTRO_CTOS")
	private String descripcionCentroCostos;
	
	@Column(name = "JERARQUIA")
	private String jerarquia;

	@Column(name = "IMPORTE_NETO")
	private Double importeNeto;

}
