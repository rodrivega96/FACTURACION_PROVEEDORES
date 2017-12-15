package com.vates.facpro.service.web.dto;

import java.util.List;

import com.vates.facpro.persistence.domain.OrdenPrefView;

import lombok.Data;

@Data
public class PrefaResponseDTO {

    private int status;
    private String message;
    private List<OrdenPrefView> ocs; 
    private List<PeoplePrefDTO> peoples;
    
}
