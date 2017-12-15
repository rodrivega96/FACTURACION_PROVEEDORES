package com.vates.facpro.business.states.oc;

import java.util.HashSet;
import java.util.Set;

import com.vates.facpro.persistence.domain.Orden;

public class Vigente extends EstadoOC{
	public static final Long STATE_ID = EstadosOC.VIGENTE;
	public static EstadoOC estado;
	
	public static EstadoOC getInstancia(){
		if(estado==null){
			estado = new Vigente();
		}
		return estado;
	}

	@Override
	public EstadoOC getProximoEstado(Orden orden) {
		if(orden.getSaldo()>0.0D){
			return Cerrada.getInstancia();
		}
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
		return "Vigente";
	}

	@Override
	public Set<EstadoOC> getProximosEstados() {
		Set<EstadoOC> estados = new HashSet<EstadoOC>();
		estados.add(Vigente.getInstancia());
		estados.add(Cerrada.getInstancia());
		return estados;
	}
}
