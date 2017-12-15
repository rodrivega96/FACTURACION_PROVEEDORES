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

/**
 * @author Lucas Scarlatta
 * 
 */

@Entity
@Table(name = "UNIDADES")
@XmlRootElement(name = "unidad")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true, exclude = { "nombre" })
@NoArgsConstructor
@Getter
@Setter
public class Unidad implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 585639640790552352L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UNIDAD_SEQ")
	@SequenceGenerator(name = "UNIDAD_SEQ", sequenceName = "UNIDAD_SEQ", allocationSize = 1)
	private Long id;

	@NotNull
	@Column(name = "NOMBRE")
	private String nombre;

}
