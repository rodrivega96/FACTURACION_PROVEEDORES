package com.vates.facpro.persistence.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "VIS_AUTORIZ_FAC_PROVEEDOR@FACTURACION")
@XmlRootElement(name = "facturaAdm")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true, exclude = { "asientos", "factura" })
@NoArgsConstructor
@Getter
@Setter
public class FacturaAdm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7752078602304910201L;

	public static Long A_VENCER = 3L;

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "CUIT")
	private String cuit;

	@Column(name = "RAZON_SOCIAL")
	private String razonSocial;

	@Column(name = "NRO_FACTURA")
	private String nroFactura;

	@Column(name = "DESCRIPCION")
	private String descripcion;

	@Column(name = "FECHA_VENCIMIENTO")
	private Date fechaVencimiento;

	@Column(name = " FECHA_CONTABLE")
	private Date fechaContable;
	
	@Column(name = " FECHA_FACTURA")
	private Date fechaFactura;
	
    @OneToMany(mappedBy = "id", fetch = FetchType.EAGER)
    private Set<FacturaAsientoAdm> asientos;

	@OneToOne(fetch = FetchType.EAGER, mappedBy = "facturaAdm")
	private Factura factura;

}
