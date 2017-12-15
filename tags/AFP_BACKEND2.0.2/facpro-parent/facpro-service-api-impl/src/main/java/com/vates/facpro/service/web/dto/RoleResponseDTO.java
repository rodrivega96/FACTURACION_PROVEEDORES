package com.vates.facpro.service.web.dto;

import java.util.List;

import lombok.Data;

@Data
public class RoleResponseDTO {

	private int status;
    private String message;
    private Long userId;
    private List<RoleDTO> roles;
    
}
