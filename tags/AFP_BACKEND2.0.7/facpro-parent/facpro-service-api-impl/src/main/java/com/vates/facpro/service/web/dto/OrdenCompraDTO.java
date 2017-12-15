package com.vates.facpro.service.web.dto;
import java.util.List;

import lombok.Data;

@Data
public class OrdenCompraDTO {
	
	private Long id;
	private Long clienteId;
	private String contacto;
	private Long tipo;
	private Long estado;
	private Long monedaId;
	private String numero;
	private String concepto;
	private Long condicionPago;
	private String fechaEmision;
	private String fechaVencimiento;
	private Double total;
	private Long activa;
	private String idTmp;
    private Integer version;
	private List<ItemOrdenDTO> items;
	private List<ProyectoDTO> proyectos;
	private List<ArchivoOrdenDTO> archivos;
 
}

