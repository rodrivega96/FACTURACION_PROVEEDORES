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
@Table(name = "OC_CLIENTE@PREFA")
@XmlRootElement(name = "cliente")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true)
@NoArgsConstructor
@Getter
@Setter
public class Cliente implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1506055213543856159L;

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "NOMBRE")
	private String nombre;

	@Column(name = "CONTACTO")
	private String contacto;

}
