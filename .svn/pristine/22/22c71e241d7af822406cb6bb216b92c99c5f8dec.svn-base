package com.vates.facpro.persistence.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * 
 * @author Cabrera Manuel
 * 
 */
@Entity
@Table(name = "OC_PEOPLES@PREFA")
@XmlRootElement(name = "people")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true)
@NoArgsConstructor
@Getter
@Setter
public class People implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -4281676317322505978L;

	@Id
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "PREFA_ID")
	private Long prefaId;

	@Column(name = "PERSONA")
	private String presona;
	
	@Column(name = "IMPORTE")
	private Double importe;

}
