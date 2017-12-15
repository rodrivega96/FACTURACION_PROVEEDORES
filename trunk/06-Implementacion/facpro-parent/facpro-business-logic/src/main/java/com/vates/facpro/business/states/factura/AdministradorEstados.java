package com.vates.facpro.business.states.factura;

import java.util.HashMap;
import java.util.Map;

import com.vates.facpro.persistence.domain.Factura;

public class AdministradorEstados {

	public static Map<Long, Estado> estados = new HashMap<Long, Estado>();

	static {
		estados.put(Inicial.STATE_ID, Inicial.getInstancia());
		estados.put(Autorizada.STATE_ID, Autorizada.getInstancia());
		estados.put(EnAutorizacion.STATE_ID, EnAutorizacion.getInstancia());
		estados.put(Rechazada.STATE_ID, Rechazada.getInstancia());
		estados.put(Observada.STATE_ID, Observada.getInstancia());
	}

	public void cambiarEstado(Factura factura) {
		factura.setEstado(getProximoEstadoId(factura.getEstado()));
	}

	public Long getProximoEstadoId(Estado estado) {
		return getProximoEstado(estado.getEstadoId()).getEstadoId();
	}

	public Long getProximoEstadoId(Long estadoId) {
		return getProximoEstado(estadoId).getEstadoId();
	}

	public Estado getProximoEstado(Long estadoId) {
		return estados.get(estadoId);
	}

	public Estado getProximoEstado(Estado estado) {
		return estados.get(estado.getEstadoId());
	}

	public Estado getEstado(Factura factura) {
		return estados.get(factura.getEstado());
	}

}
