package com.vates.facpro.service.web.dto;
import lombok.Data;

@Data
public class ProyectoDTO {
	
	private Long id;
	private String nombre;
	private String activo;
	private Boolean seleccionado;
	private String pm;
	private Boolean extendido;
	private Boolean canDelete;
 
}

