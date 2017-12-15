package com.vates.facpro.service.web.dto;

import java.util.List;
import java.util.Set;

import lombok.Data;

@Data
public class HistorialAutorizacionDTO {

	private String cuit;
	private Double importeNeto;
	private Double iva;
	private Double total;
	private String razonSocial;
	private String vencimiento;
	private String fechaFactura;
	private String nroFactura;
	private String descripcion;
	private List<HistorialWFDTO> historials;
	private Set<AsientoFullDTO> asientos;

}
