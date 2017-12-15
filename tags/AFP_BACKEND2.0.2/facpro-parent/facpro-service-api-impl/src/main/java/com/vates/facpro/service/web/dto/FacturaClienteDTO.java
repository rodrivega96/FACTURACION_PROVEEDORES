package com.vates.facpro.service.web.dto;

import lombok.Data;

@Data
public class FacturaClienteDTO {

	private String numero;
	private String nroConformidad;
	private Double total;
}
