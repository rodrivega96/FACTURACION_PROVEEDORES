package com.vates.facpro.persistence.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "FACTURA")
@XmlRootElement(name = "factura")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true, exclude = {
		"nivel", "facturaAdm" })
@NoArgsConstructor
@Getter
@Setter
public class Factura extends AbstractBaseEntity implements Serializable {
	/**
	 * 
	 */
	public static String OPT_YES = "Si";
	public static String OPT_NO = "No";

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FACTURA_SEQ")
	@SequenceGenerator(name = "FACTURA_SEQ", sequenceName = "FACTURA_SEQ", allocationSize = 1)
	private Long id;

	private static final long serialVersionUID = 2359789429048557216L;
	@NotNull
	@Column(name = "ESTADO")
	private Long estado;
	
	@Column(name="FAC_ADM")
	private Long facturaAdm;

	@Column(name = "VENCIMIENTO")
	private Date vencimiento;

	@Column(name = "TIPO_ID")
	@NotNull
	private Long tipoFactura;

	@Column(name = "WF_CREATED_USER")
	private Long wfCreatedUser;

	@Column(name = "WF_UPDATED_USER")
	private Long wfUpdatedUser;

	@JoinColumn(name = "NIVEL_ID", referencedColumnName = "ID")
	@ManyToOne(fetch = FetchType.EAGER)
	private Nivel nivel;

	@OneToMany(mappedBy = "factura", fetch = FetchType.EAGER)
	private Set<NivelPublicado> nivelesPublicados;

}
