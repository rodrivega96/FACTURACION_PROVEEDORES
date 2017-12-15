package com.vates.facpro.service.web.dto;

import lombok.Data;

@Data
public class ItemOrdenDTO {

	private Long id;
	private Long cantidad;
	private String descripcion;
	private Double precio;
	private Double total;
	private Long unidad;
	private boolean extendido;

}
