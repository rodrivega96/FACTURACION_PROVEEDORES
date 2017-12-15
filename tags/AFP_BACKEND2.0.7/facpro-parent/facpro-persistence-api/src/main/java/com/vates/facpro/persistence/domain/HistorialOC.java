package com.vates.facpro.persistence.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Cabrera Manuel
 * 
 */

@Entity
@Table(name = "HISTORIAL")
@XmlRootElement(name = "historial")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true)
@NoArgsConstructor
@Getter
@Setter
public class HistorialOC implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1157351802642673848L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HISTORIAL_SEQ")
	@SequenceGenerator(name = "HISTORIAL_SEQ", sequenceName = "HISTORIAL_SEQ", allocationSize = 1)
	private Long id;

	@NotNull
	@Column(name = "CREATED_BY")
	private Long createdBy;
	
    @NotNull
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;


	@NotNull
	@Column(name = "STATE_POST_ID")
	private Long estadoPrevio;
	
	@NotNull
	@Column(name = "STATE_PREV_ID")
	private Long estadoPosterior;
	
	@NotNull
	@Column(name = "ORDEN_ID")
	private Long ordenId;

}
