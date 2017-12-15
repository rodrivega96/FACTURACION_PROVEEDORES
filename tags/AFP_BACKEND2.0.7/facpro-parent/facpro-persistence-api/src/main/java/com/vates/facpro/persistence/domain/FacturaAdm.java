package com.vates.facpro.persistence.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

	@Column(name = "FECHA_CONTABLE")
	private Date fechaContable;
	
	@Column(name = "FECHA_FACTURA")
	private Date fechaFactura;
	
	@Column(name = "GRAL_NETO")
	private Double gralNeto;

	@Column(name = "GRAL_GRAV_IVA")
	private Double gralGravIva;
	
	@Column(name = "GRAL_NO_GRAV_IVA")
	private Double gralNoGravIva;
	
	@Column(name = "GRAL_IMP_INT")
	private Double gralImpInt;

	@Column(name = "GRAL_IVA_INC")
	private Double gralIvaInc;
	
	@Column(name = "GRAL_IVA_NO_INC")
	private Double gralIvaNoInc;
	
	
	@Column(name = "GRAL_BRUTO")
	private Double gralBruto;

    @OneToMany(mappedBy = "id", fetch = FetchType.EAGER)
    private List<FacturaAsientoAdm> asientos;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID", referencedColumnName = "FAC_ADM",insertable = false, updatable = false)
	private Factura factura;

}
