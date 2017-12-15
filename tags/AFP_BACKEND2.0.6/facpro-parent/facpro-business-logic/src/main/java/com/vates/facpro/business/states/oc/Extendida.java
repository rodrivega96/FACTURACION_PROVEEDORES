package com.vates.facpro.business.states.oc;

import java.util.HashSet;
import java.util.Set;

import com.vates.facpro.persistence.domain.Orden;

public class Extendida extends EstadoOC{
	public static final Long STATE_ID = EstadosOC.EXTENDIDA;
	public static EstadoOC estado;
	
	public static EstadoOC getInstancia(){
		if(estado==null){
			estado = new Extendida();
		}
		return estado;
	}

	@Override
	public EstadoOC getProximoEstado(Orden orden) {
		return Cerrada.getInstancia();
	}


	@Override
	public boolean validarEstado() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Long getEstadoId() {
		// TODO Auto-generated method stub
		return STATE_ID;
	}

	@Override
	public String getNombre() {
		// TODO Auto-generated method stub
		return "Extendida";
	}

	@Override
	public Set<EstadoOC> getProximosEstados() {
		Set<EstadoOC> estados = new HashSet<EstadoOC>();
		estados.add(Extendida.getInstancia());
		estados.add(Cerrada.getInstancia());
		return estados;
	}
}
