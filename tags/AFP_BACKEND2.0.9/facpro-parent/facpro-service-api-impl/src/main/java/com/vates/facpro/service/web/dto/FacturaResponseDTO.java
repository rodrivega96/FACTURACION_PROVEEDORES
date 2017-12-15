package com.vates.facpro.service.web.dto;

import lombok.Data;

@Data
public class FacturaResponseDTO {

	private int status;
	private String message;
	private FacturaFullDTO factura;
	private HistorialAutorizacionDTO historialDTO;

}
