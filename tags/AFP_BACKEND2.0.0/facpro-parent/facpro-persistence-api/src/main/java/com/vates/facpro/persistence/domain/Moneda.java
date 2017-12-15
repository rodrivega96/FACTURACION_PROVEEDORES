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
@Table(name = "OC_MONEDA@FACTURACION")
@XmlRootElement(name = "moneda")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true)
@NoArgsConstructor
@Getter
@Setter
public class Moneda implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5760524506983572563L;

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "NOMBRE")
	private String nombre;

	@Column(name = "SIMBOLO")
	private String simbolo;

}
