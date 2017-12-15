package com.vates.facpro.service.web.dto;

import java.util.List;

import lombok.Data;

@Data
public class DatoInicialOrdenDTO {

	private List<TipoOrdenDTO> tipoList;
	private List<UnidadDTO> unidadList;
	private List<EstadoDTO> estados;
	private List<MonedaDTO> monedaList;
	private List<ClienteDTO> clientes;
	private List<EstadoPreFacDTO> estadosPreFac;
	private Long sizeMax;

}
