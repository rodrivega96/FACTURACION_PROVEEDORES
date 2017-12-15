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
 * Entity representing a system's user.
 * 
 * @author Cabrera Manuel
 * 
 */
@Entity
@Table(name = "KANAV_STATE@KANAV")
@XmlRootElement(name = "kanav")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true)
@NoArgsConstructor
@Getter
@Setter
public class Kanav implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5363398538829355183L;

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "STATE")
	private String state;

}
