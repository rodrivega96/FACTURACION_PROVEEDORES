package com.vates.facpro.persistence.domain;

import java.util.List;

import lombok.Data;

@Data
public class ProveedorView {

	private String id;
	private String razonSocial;
	private String cuit;
	private String iibb;
	private String contacto;
	private String direccion;
	private Long tipoTelefono;
	private String numeroTelefono;
	private String email;
	private Long medioPagoDefecto;
	private List<Long> mediosPago;
	private String mediosPagoString;
	private String descuento;
	private String observaciones;
}
