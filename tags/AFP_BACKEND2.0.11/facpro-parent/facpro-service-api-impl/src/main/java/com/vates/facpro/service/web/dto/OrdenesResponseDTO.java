package com.vates.facpro.service.web.dto;

import lombok.Data;

@Data
public class OrdenesResponseDTO {

    private int status;
    private String message;
    private DatoInicialOrdenDTO datoInicialOrdenDTO;
    
}
