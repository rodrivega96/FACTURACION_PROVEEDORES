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
	private String montoOC;
	private String totalFacturado;
	private String saldoOC;
	private String fechaVencimiento;
	private Long clienteId;
	private Boolean activa;
}
