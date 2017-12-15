package com.vates.facpro.security.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @author Gaston Napoli
 * 
 */
@Data
public class MenuDTO implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 6200885406779080643L;

    private String title;

    private List<SubMenuDTO> subOptions;
}
