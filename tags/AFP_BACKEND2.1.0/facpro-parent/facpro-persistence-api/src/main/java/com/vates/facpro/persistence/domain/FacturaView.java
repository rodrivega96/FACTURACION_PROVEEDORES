package com.vates.facpro.persistence.domain;

import lombok.Data;

@Data
public class FacturaView {

    private String estado;
    private Long id;
    private Long idFacturaAdm;
    private String vencimiento;
	private String tipoFactura;
	private String cuit;
	private String razonSocial;
	private String nroFactura;
	private boolean canLoadWf;
	private boolean canLoadFile;
	private String workflow;
	private String cuentaContable;
	private String centroCosto;
	private String fechaVencimiento;
	private String fechaContable;
	private String fechaFactura;
	private String descripcion;
	private String diasVencer;
	private String importeNeto;
    private String autorizaciones;
    private String importeTotal;
    private String iva;
    private String observacion;
}
