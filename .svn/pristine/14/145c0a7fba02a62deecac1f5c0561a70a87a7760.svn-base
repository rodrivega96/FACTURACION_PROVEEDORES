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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Entity representing a system's user.
 * 
 * @author Gaston Napoli
 * 
 */
@Entity
@Table(name = "HISTORIAL_AUTORIZACION")
@XmlRootElement(name = "historialAutorizacion")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true)
@NoArgsConstructor
@Getter
@Setter
public class HistorialAutorizacion extends AbstractBaseEntity implements
		Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5159579078512531778L;

	public static Long PENDIENTE = 0L;
	public static Long AUTORIZADA = 1L;
	public static Long AUTORIZADA_SUPERIOR = 2L;
	public static Long RECHAZADA = 3L;
	public static Long RECHAZADA_SUPERIOR = 4L;
	public static Long OBSERVADA = 5L;
	public static Long OBSERVADA_SUPERIOR = 6L;
	
	public static String OBSERVADA_SUPER = "Observada por nivel superior";
	public static String RECHAZADA_SUPER = "Rechazada por nivel superior";
	public static String AUTORIZADA_SUPER = "Autorizada por nivel superior";


	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HISTORIAL_AUTORIZACION_SEQ")
	@SequenceGenerator(name = "HISTORIAL_AUTORIZACION_SEQ", sequenceName = "HISTORIAL_AUTORIZACION_SEQ", allocationSize = 1)
	private Long id;

	@JsonIgnore
	@JoinColumn(name = "FACTURA_ID", referencedColumnName = "ID")
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	private Factura factura;

	@Column(name = "ESTADO")
	@NotNull
	private Long estado;

	@Column(name = "DESCRIPCION")
	private String descripcion;

	@JsonIgnore
	@JoinColumn(name = "NIVEL_ID", referencedColumnName = "ID")
	@ManyToOne(fetch = FetchType.EAGER)
	private Nivel nivel;

}
