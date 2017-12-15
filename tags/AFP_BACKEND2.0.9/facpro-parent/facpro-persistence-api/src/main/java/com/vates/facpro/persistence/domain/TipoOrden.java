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
 * Entity representing a system's user.
 * 
 * @author Gaston Napoli
 * 
 */
@Entity
@Table(name = "TIPO_ORDEN")
@XmlRootElement(name = "tipoOrden")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true)
@NoArgsConstructor
@Getter
@Setter
public class TipoOrden implements Serializable {

	public static Long EXTERNA = 1L;
	public static Long INTERNA = 2L;
	
	private static final long serialVersionUID = 8420521526947095519L;
	/**
	 * 
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TIPO_ORDEN_SEQ")
	@SequenceGenerator(name = "TIPO_ORDEN_SEQ", sequenceName = "TIPO_ORDEN_SEQ", allocationSize = 1)
	private Long id;

	@Column(name = "NOMBRE")
	@NotNull
	private String nombre;

}
