package com.vates.facpro.persistence.domain;

import lombok.Data;

@Data
public class PreFacturacionView {
	private String estado;
	private Long estadoId;
	private Long id;
	private String proyecto;
	private String cliente;
	private String manager;
	private String descripcion;
	private String periodo;
	private Double importe;

}
