package com.vates.facpro.service.web.dto;

import java.util.List;

import lombok.Data;

@Data
public class NivelResponseDTO {

    private int status;
    private String message;
    private NivelDTO nivelDTO;
    private List<NivelDTO> nivelesDTO;
    
}
