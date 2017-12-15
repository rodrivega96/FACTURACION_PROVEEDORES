package com.vates.facpro.service.web.dto;

import java.util.Date;

import lombok.Data;

@Data
public class HistorialWFDTO {

	private Integer nivel;
	private String autoriza;
	private Long estado;
	private String fecha;
	private String descripcion;
	private Date update;
	private Long userId;

}
