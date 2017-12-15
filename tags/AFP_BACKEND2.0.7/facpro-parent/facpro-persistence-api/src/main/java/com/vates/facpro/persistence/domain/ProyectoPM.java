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
@Table(name = "OC_PROYECTO_PM@PREFA")
@XmlRootElement(name = "proyectoPm")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true)
@NoArgsConstructor
@Getter
@Setter
public class ProyectoPM implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1177263137732894982L;

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "PROYECTO_ID")
	private Long proyectoId;
	
	@Column(name = "ID_PM")
	private Long idPm;

	@Column(name = "PM")
	private String pm;
	
	@Column(name = "RESPONSABLE")
	private Integer responsable;
	

}
