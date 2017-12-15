package com.vates.facpro.service.web.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProyectosResponseDTO {

    private int status;
    private String message;
    private List<ProyectoDTO> proyectosDTO;
    private List<ManagerDTO> managersDTO;
    
}
