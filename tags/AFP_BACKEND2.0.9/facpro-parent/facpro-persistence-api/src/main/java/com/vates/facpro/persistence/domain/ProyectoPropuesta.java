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
@Table(name = "OC_PROYECTO_PROPUESTA@PREFA")
@XmlRootElement(name = "preoyectoPropuesta")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true)
@NoArgsConstructor
@Getter
@Setter
public class ProyectoPropuesta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3811884091852199101L;

	@Id
	@Column(name = "PROYECTO_ID")
	private Long proyectoId;

	@Id
	@Column(name = "PROPUESTA_ID")
	private Long propuestaId;
	

}
