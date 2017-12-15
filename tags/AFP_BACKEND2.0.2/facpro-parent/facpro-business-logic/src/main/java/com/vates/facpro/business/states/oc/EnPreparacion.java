package com.vates.facpro.business.states.oc;

import java.util.HashSet;
import java.util.Set;

import com.vates.facpro.persistence.domain.Orden;

public class EnPreparacion extends EstadoOC{
	public static final Long STATE_ID = EstadosOC.EN_PREPRARACION;
	public static EstadoOC estado;
	
	public static EstadoOC getInstancia(){
		if(estado==null){
			estado = new EnPreparacion();
		}
		return estado;
	}

	@Override
	public EstadoOC getProximoEstado(Orden orden) {
		return Vigente.getInstancia();
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
		return "En Preparacion";
	}

	@Override
	public Set<EstadoOC> getProximosEstados() {
		Set<EstadoOC> estados = new HashSet<EstadoOC>();
		estados.add(EnPreparacion.getInstancia());
		estados.add(Vigente.getInstancia());
		return estados;
	}
}
