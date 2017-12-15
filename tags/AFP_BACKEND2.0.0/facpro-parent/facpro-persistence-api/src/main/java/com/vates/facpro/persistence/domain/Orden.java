package com.vates.facpro.persistence.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Lucas Scarlatta
 * 
 */

@Entity
@Table(name = "ORDENES")
@XmlRootElement(name = "orden")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true, exclude = {
		"itemsOrden", "ocProyectos" })
@NoArgsConstructor
@Getter
@Setter
public class Orden extends AbstractBaseEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8048731177190453785L;
	
	public static final Long ACTIVA = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDEN_SEQ")
	@SequenceGenerator(name = "ORDEN_SEQ", sequenceName = "ORDEN_SEQ", allocationSize = 1)
	private Long id;

	@NotNull
	@Column(name = "CLIENTE_ID")
	private Long clienteId;

	@NotNull
	@Column(name = "CONTACTO")
	private String contacto;

	@Column(name = "TIPO_ID")
	@NotNull
	private Long tipo;

	@NotNull
	@Column(name = "ESTADO")
	private Long estado;

	@NotNull
	@Column(name = "OC_CREATED_USER")
	private Long createdBy;

	@NotNull
	@Column(name = "OC_UPDATED_USER")
	private Long updatedBy;

	@NotNull
	@Column(name = "MONEDA_ID")
	private Long monedaId;

	@NotNull
	@Column(name = "NUMERO")
	private String numero;

	@NotNull
	@Column(name = "CONCEPTO")
	private String concepto;

	@NotNull
	@Column(name = "CONDICION_PAGO")
	private Long condicionPago;

	@NotNull
	@Column(name = "EMISION")
	private Date fechaEmision;

	@NotNull
	@Column(name = "VENCIMIENTO")
	private Date fechaVencimiento;

	@NotNull
	@Column(name = "TOTAL")
	private Double total;

	@Column(name = "ACTIVA")
	private Long activa;
	
	@Column(name = "NUMERO_OC_INTERNA_PREFIX")
	private Long numeroInternaPrefix = 0L;
	
	@Column(name = "NUMERO_OC_INTERNA_SUFIX")
	private Long numeroInternaSufix;
	
	@JoinColumn(name = "ORDEN_ID", referencedColumnName = "ID")
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ItemOrden> itemsOrden;

	@JoinColumn(name = "ID_OC", referencedColumnName = "ID")
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<OcProyecto> ocProyectos;

	public Double getSaldo() {
		return 0.0d;
	}

}
