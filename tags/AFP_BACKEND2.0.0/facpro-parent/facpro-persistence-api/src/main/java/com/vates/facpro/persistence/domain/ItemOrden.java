package com.vates.facpro.persistence.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.annotations.Type;

/**
 * @author Lucas Scarlatta
 * 
 */

@Entity
@Table(name = "ITEMS")
@XmlRootElement(name = "item")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true)
@NoArgsConstructor
@Getter
@Setter
public class ItemOrden implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 585639640790552352L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEM_SEQ")
	@SequenceGenerator(name = "ITEM_SEQ", sequenceName = "ITEM_SEQ", allocationSize = 1)
	private Long id;

	@NotNull
	@Column(name = "CANTIDAD")
	private Long cantidad;

	@NotNull
	@Column(name = "DESCRIPCION")
	private String descripcion;

	@NotNull
	@Column(name = "PRECIO")
	private Double precio;

	@NotNull
	@Column(name = "TOTAL")
	private Double total;

	@JoinColumn(name = "UNIDAD_ID", referencedColumnName = "ID")
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	private Unidad unidad;

	@NotNull
	@Column(name = "EXTENDIDO")
	@Type(type = "yes_no")
	private boolean extendido;

	@NotNull
	@Column(name = "INDICE")
	private Long indice;

}
