package com.vates.facpro.service.web.dto;

import java.util.List;

import lombok.Data;

import com.vates.facpro.persistence.domain.ProveedorView;

@Data
public class ProveedorResponseDTO {

	private int status;
	private String message;
	private ProveedorView proveedor;
	private List<ProveedorView> proveedores;
	private DatoInicialProveedorDTO datoInicialDTO;

}
