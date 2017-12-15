package com.vates.facpro.service.web.dto;
import lombok.Data;

@Data
public class PropuestaDTO {
	
	private Long id;
	private String descripcion;
	private String comercial;
	private String fechaDesde;
	private String fechaHasta;
	private Boolean seleccionado;
 
}

