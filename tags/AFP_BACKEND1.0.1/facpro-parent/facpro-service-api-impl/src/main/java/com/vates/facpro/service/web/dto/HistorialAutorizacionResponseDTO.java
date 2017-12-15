package com.vates.facpro.service.web.dto;

import lombok.Data;

@Data
public class HistorialAutorizacionResponseDTO {

	private int status;
	private String message;
	private HistorialAutorizacionDTO dto;
}
