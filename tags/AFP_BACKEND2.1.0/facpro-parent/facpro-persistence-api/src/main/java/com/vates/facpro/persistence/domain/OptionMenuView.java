package com.vates.facpro.persistence.domain;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Entity representing a system's Role.
 * 
 * @author Cabrera Manuel
 * 
 */
@ToString(callSuper = false, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true)
@Data
public class OptionMenuView implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long order;
	private String name;

}
