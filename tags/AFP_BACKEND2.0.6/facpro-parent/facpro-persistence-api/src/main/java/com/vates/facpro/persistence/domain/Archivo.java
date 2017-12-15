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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ARCHIVOS")
@XmlRootElement(name = "archivo")
@ToString(callSuper = true, doNotUseGetters = true)
@NoArgsConstructor
@Getter
@Setter
public class Archivo implements Serializable {

	private static final long serialVersionUID = -2188413573841524183L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ARCHIVO_SEQ")
	@SequenceGenerator(name = "ARCHIVO_SEQ", sequenceName = "ARCHIVO_SEQ", allocationSize = 1)
	private Long id;

	@Column(name = "CONTENIDO")
	@Lob
	private byte[] contenidoBlob;

	@JoinColumn(name = "ARCHIVO_HD_ID", referencedColumnName = "ID")
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	private ArchivoHeader archivoHeader;

}
