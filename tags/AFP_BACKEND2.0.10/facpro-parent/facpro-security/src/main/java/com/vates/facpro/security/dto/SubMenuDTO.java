package com.vates.facpro.security.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * @author Gaston Napoli
 * 
 */
@Data
public class SubMenuDTO implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = -7628367801317045406L;

    private String title;

    private String page;
}
