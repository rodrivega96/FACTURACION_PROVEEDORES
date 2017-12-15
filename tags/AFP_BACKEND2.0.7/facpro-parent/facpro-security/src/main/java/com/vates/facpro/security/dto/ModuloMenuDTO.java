package com.vates.facpro.security.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @author Gaston Napoli
 * 
 */
@Data
public class ModuloMenuDTO implements Serializable {
    

    /**
	 * 
	 */
	private static final long serialVersionUID = 3094535850466493276L;

	private String title;
    
    private List<SubMenuDTO> subOptions;

}
