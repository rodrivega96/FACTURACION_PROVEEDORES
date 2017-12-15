package com.vates.facpro.persistence.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "FACTURAS_X_OC")
@XmlRootElement(name = "facturasPorOC")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true, exclude = { "id" })
@NoArgsConstructor
@Getter
@Setter
public class FacturasClientePorOC implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4142650639087660419L;

	public static final Integer SELECTED = 1;
	public static final Integer NOT_SELECTED = 2;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FACTURAS_X_OC_SEQ")
	@SequenceGenerator(name = "FACTURAS_X_OC_SEQ", sequenceName = "FACTURAS_X_OC_SEQ", allocationSize = 1)
	private Long id;

	@NotNull
	@Column(name = "OC_ID")
	private Long idOrden;

	@NotNull
	@Column(name = "FACTURA_ID")
	private Long idFactura;

	@NotNull
	@Column(name = "USUARIO_ID")
	private Long idUsuario;

	private Double total;

}
