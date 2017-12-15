package com.vates.facpro.persistence.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Type;

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
@Table(name = "PROVEEDOR")
@XmlRootElement(name = "proveedor")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true)
@NoArgsConstructor
@Getter
@Setter
public class Proveedor extends AbstractBaseEntity implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 5225435371410511666L;

	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROVEEDOR_SEQ")
	@SequenceGenerator(name = "PROVEEDOR_SEQ", sequenceName = "PROVEEDOR_SEQ", allocationSize = 1)
	private Long id;

	@NotNull
	@Column(name = "RAZON_SOCIAL")
	private String razonSocial;
	
	@NotNull
	@Column(name = "CUIT")
	private String cuit;
	
	
	@Column(name = "IIBB")
	private String iibb;
	
	@NotNull
	@Column(name = "CONTACTO")
	private String contacto;
	

	@Column(name = "DIRECCION")
	private String direccion;
	
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="TIPO_TELEFONO_ID", referencedColumnName="ID")
	private TipoTelefono tipoTelefono;

	@Column(name = "NUMERO_TELEFONO")
	private String numeroTelefono;
	
	@Column(name = "EMAIL")
	private String email;
	

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="MEDIO_PAGO_ID", referencedColumnName="ID")
	@NotNull
	private MedioPago medioPagoDefecto;
	
	@ManyToMany(fetch=FetchType.EAGER)
	
	@JoinTable(name="PROVEEDOR_MEDIO_PAGO",
	joinColumns = { @JoinColumn(name = "PROVEEDOR_ID", referencedColumnName = "ID") }, 
	inverseJoinColumns = { @JoinColumn(name = "MEDIO_ID", referencedColumnName = "ID") })
	private List<MedioPago> mesdiosPago;
	
	@Column(name = "DESCUENTO")
	private String descuento;
	
	@Column(name = "OBSERVACIONES")
	private String observaciones;
	
	@NotNull
	@Column(name = "CREATED_BY")
	private Long createdBy;

	@NotNull
	@Column(name = "UPDATED_BY")
	private Long updatedBy;
	
	@Column(name = "DELETED")
	@NotNull
	@Type(type = "yes_no")
	private boolean deleted=false;

}
