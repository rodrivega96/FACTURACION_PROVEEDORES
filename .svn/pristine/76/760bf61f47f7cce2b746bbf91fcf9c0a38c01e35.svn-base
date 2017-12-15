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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Type;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity representing a Email Notification
 * 
 * @author Cabrera Manuel
 * 
 */
@Entity
@Table(name = "NOTIFICATION")
@XmlRootElement(name = "notification")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true)
@NoArgsConstructor
@Getter
@Setter
public class Notification implements Serializable {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 301027823944126302L;
	
	public static final Integer TYPE_AUTHORIZATION =1;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTIFICATION_SEQ")
	@SequenceGenerator(name = "NOTIFICATION_SEQ", sequenceName = "NOTIFICATION_SEQ", allocationSize = 1)
	private Long id;

	@NotNull
	@Column(name = "NTO")
	private String to;
	
	@Column(name = "NCC")
	private String cc;
	
	@Column(name = "NCCO")
	private String cco;
	
	@NotNull
	@Size(min = 1, max = 4000)
	@Column(name = "NSUBJECT")
	private String subject;
	
	@Column(name = "NBODY", columnDefinition="CLOB NOT NULL")
	private String body;
	
	@NotNull
	@Column(name = "NIMPORTANT")
	@Type(type="yes_no")
	private Boolean important;
	
	@NotNull
	@Column(name = "NTYPE")
	private Integer type;

}
