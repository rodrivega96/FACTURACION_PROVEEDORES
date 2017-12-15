package com.vates.facpro.service.web.dto;

import java.util.List;

import lombok.Data;

@Data
public class PropuestasResponseDTO {

    private int status;
    private String message;
    private List<PropuestaDTO> propuestasDTO;
    
}
