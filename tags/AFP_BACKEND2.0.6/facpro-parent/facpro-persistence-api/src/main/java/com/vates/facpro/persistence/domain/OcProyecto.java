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

import org.hibernate.annotations.Type;

/**
 * 
 * 
 * @author Cabrera Manuel
 * 
 */
@Entity
@Table(name = "OC_X_PROYECTO")
@XmlRootElement(name = "ocProyecto")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true)
@NoArgsConstructor
@Getter
@Setter
public class OcProyecto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1443539204388487730L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OC_X_PROYECTO_SEQ")
	@SequenceGenerator(name = "OC_X_PROYECTO_SEQ", sequenceName = "OC_X_PROYECTO_SEQ", allocationSize = 1)
	private Long id;

	@NotNull
	@Column(name = "ID_PROYECTO")
	private Long proyectoId;

	@NotNull
	@Column(name = "EXTENDIDO")
	@Type(type = "yes_no")
	private boolean extendido;

}
