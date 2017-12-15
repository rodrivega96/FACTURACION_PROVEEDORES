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
 * Entity representing a system's user.
 * 
 * @author Gaston Napoli
 * 
 */
@Entity
@Table(name = "ARCHIVO_HEADERS")
@XmlRootElement(name = "archivoHeader")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true)
@NoArgsConstructor
@Getter
@Setter
public class ArchivoHeader extends AbstractBaseEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2461040102039843871L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ARCHIVO_HD_SEQ")
	@SequenceGenerator(name = "ARCHIVO_HD_SEQ", sequenceName = "ARCHIVO_HD_SEQ", allocationSize = 1)
	private Long id;

	@Column(name = "NAME")
	@NotNull
	private String name;

	@Column(name = "TYPE")
	@NotNull
	private String type;

	@Column(name = "DELETED")
	@Type(type = "yes_no")
	private Boolean deleted;

}
