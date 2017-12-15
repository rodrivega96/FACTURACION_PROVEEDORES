package com.vates.facpro.business.states;

public abstract class Estado {
	
	
	
	protected Estado() {
		super();
	}
	
	public abstract Estado getProximoEstado();
	
	public abstract Long getEstadoId();
	
	public abstract String getNombre();
	
	public abstract boolean validarEstado();

}
