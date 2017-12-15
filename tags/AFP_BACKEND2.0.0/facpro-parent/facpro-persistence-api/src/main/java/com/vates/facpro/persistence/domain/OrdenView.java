package com.vates.facpro.persistence.domain;

import lombok.Data;

@Data
public class OrdenView {
    private String estado;
    private Long estadoId;
    private Long id;
    private String numero;
    private String concepto;
	private String proyecto;
	private String cliente;
	private String fechaEmision;
	private String Facturas;
	private Double montoOC;
	private Double totalFacturado;
	private Double saldoOC;
	private String fechaVencimiento;
}
