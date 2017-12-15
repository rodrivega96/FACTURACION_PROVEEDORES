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
@Table(name = "ARCHIVO_FACTURA")
@XmlRootElement(name = "archivoFactura")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true)
@NoArgsConstructor
@Getter
@Setter
public class ArchivoFactura extends AbstractBaseEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1609458664100159906L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ARCHIVO_FACTURA_SEQ")
	@SequenceGenerator(name = "ARCHIVO_FACTURA_SEQ", sequenceName = "ARCHIVO_FACTURA_SEQ", allocationSize = 1)
	private Long id;

	@JoinColumn(name = "FACTURA_ID", referencedColumnName = "ID")
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	private Factura factura;

	@JoinColumn(name = "ARCHIVO_HEADER_ID", referencedColumnName = "ID")
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private ArchivoHeader archivoHeader;

}
