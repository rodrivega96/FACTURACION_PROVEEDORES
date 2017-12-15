package com.vates.facpro.business.states.oc;

import java.util.Set;

import com.vates.facpro.persistence.domain.Orden;

public abstract class EstadoOC {
	
	
	
	protected EstadoOC() {
		super();
	}
	
	public abstract EstadoOC getProximoEstado(Orden orden);
	
	public abstract Set<EstadoOC> getProximosEstados();
	
	public abstract Long getEstadoId();
	
	public abstract String getNombre();
	
	public abstract boolean validarEstado();

}
