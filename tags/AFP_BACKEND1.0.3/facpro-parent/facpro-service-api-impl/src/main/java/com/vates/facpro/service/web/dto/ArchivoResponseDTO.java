package com.vates.facpro.service.web.dto;

import lombok.Data;

@Data
public class ArchivoResponseDTO {

    private int status;
    private String message;
    private ArchivoHeaderDTO headerDTO;
    private ArchivoDTO dto;
    
}
