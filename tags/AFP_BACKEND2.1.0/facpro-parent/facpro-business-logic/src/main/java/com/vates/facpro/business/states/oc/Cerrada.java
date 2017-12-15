package com.vates.facpro.business.states.oc;

import java.util.HashSet;
import java.util.Set;

import com.vates.facpro.persistence.domain.Orden;

public class Cerrada extends EstadoOC{
	public static final Long STATE_ID = EstadosOC.CERRADA;
	public static EstadoOC estado;
	
	public static EstadoOC getInstancia(){
		if(estado==null){
			estado = new Cerrada();
		}
		return estado;
	}

	@Override
	public EstadoOC getProximoEstado(Orden orden) {
		// TODO Auto-generated method stub
		return Extendida.getInstancia();
	}


	@Override
	public boolean validarEstado() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Long getEstadoId() {
		return STATE_ID;
	}

	@Override
	public String getNombre() {
		// TODO Auto-generated method stub
		return "Cerrada";
	}

	@Override
	public Set<EstadoOC> getProximosEstados() {
		Set<EstadoOC> estados = new HashSet<EstadoOC>();
		estados.add(Cerrada.getInstancia());
		return estados;
	}
}
