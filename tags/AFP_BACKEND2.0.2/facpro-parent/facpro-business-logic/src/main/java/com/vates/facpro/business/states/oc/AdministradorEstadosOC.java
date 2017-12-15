package com.vates.facpro.business.states.oc;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.vates.facpro.persistence.domain.HistorialOC;
import com.vates.facpro.persistence.domain.Orden;

public class AdministradorEstadosOC {

	public static Map<Long, EstadoOC> estados = new HashMap<Long, EstadoOC>();
	public static Set<EstadoOC> estadosIniciales = new HashSet<EstadoOC>();
	public static String STATE_CHANGE = "STATE_CHANGE";

	static {
		estados.put(EnPreparacion.STATE_ID, EnPreparacion.getInstancia());
		estados.put(Vigente.STATE_ID, Vigente.getInstancia());
		estados.put(Extendida.STATE_ID, Extendida.getInstancia());
		estados.put(Cerrada.STATE_ID, Cerrada.getInstancia());
		estadosIniciales.add(EnPreparacion.getInstancia());
		estadosIniciales.add(Vigente.getInstancia());
	}

	public static HistorialOC cambiarEstado(Orden orden, Long userId) {
		synchronized (STATE_CHANGE) {
			HistorialOC hist = new HistorialOC();
			hist.setCreatedAt(new Date());
			hist.setCreatedBy(userId);
			hist.setEstadoPrevio(orden.getEstado());
			orden.setEstado(estados.get(orden.getEstado()).getProximoEstado(orden)
					.getEstadoId());
			hist.setEstadoPosterior(orden.getEstado());
			hist.setOrdenId(orden.getId());
			return hist;
		}
	}
	
	public static HistorialOC cambiarEstado(Orden orden,Long state,Long userId) {
		HistorialOC hist = new HistorialOC();
		hist.setCreatedAt(new Date());
		hist.setCreatedBy(userId);
		hist.setEstadoPrevio(orden.getEstado());
		orden.setEstado(state);
		hist.setEstadoPosterior(orden.getEstado());
		hist.setOrdenId(orden.getId());
		return hist;
	}

	public static Long getProximoEstadoId(Orden orden) {
		synchronized (STATE_CHANGE) {
			return estados.get(orden.getEstado()).getProximoEstado(orden).getEstadoId();
		}
	}

	public static EstadoOC getProximoEstado(Orden orden) {
		synchronized (STATE_CHANGE) {
			return estados.get(orden.getEstado()).getProximoEstado(orden);
		}
	}

	public static EstadoOC getEstado(Orden orden) {
		synchronized (STATE_CHANGE) {
			return estados.get(orden.getEstado());
		}
	}

}
