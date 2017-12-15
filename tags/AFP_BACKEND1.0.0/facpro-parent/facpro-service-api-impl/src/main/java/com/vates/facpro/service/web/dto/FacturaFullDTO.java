package com.vates.facpro.service.web.dto;

import java.util.Date;
import java.util.Set;

import lombok.Data;

@Data
public class FacturaFullDTO {

	private Long id;
	private Integer version;
    private String estado;
    private Long idFacturaAdm;
    private Date vencimiento;
	private Long tipoFactura;
	private String cuit;
	private String razonSocial;
	private String nroFactura;
	private String descripcion;
	private String fechaVencimientoAdm;
	private String fechaContableAdm;
	private Set<AsientoFullDTO> asientos;
	private Long nroControl;
	private String descControl;
	private Double importeNeto;
	private boolean pendiente;
	private boolean canSave;
	private String fechaFactura;
    
}
