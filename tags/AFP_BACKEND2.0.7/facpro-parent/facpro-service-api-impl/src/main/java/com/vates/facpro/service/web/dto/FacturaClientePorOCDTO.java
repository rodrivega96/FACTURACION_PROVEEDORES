package com.vates.facpro.service.web.dto;

import java.util.List;

import lombok.Data;

@Data
public class FacturaClientePorOCDTO {

	private Long ordenId;
	private List<FacturaClienteDTO> facturaCliente;

}
