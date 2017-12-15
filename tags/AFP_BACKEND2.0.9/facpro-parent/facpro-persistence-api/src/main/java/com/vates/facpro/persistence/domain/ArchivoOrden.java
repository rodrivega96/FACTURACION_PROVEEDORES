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
import javax.xml.bind.annotation.XmlRootElement;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ARCHIVO_ORDEN")
@XmlRootElement(name = "archivoOrden")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true)
@NoArgsConstructor
@Getter
@Setter
public class ArchivoOrden implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7428026449153088047L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ARCHIVO_ORDEN_SEQ")
	@SequenceGenerator(name = "ARCHIVO_ORDEN_SEQ", sequenceName = "ARCHIVO_ORDEN_SEQ", allocationSize = 1)
	private Long id;

	@JoinColumn(name = "ORDEN_ID", referencedColumnName = "ID")
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	private Orden orden;

	@JoinColumn(name = "ARCHIVO_HEADER_ID", referencedColumnName = "ID")
	@ManyToOne(fetch = FetchType.EAGER, cascade= CascadeType.ALL)
	private ArchivoHeader archivoHeader;

}
