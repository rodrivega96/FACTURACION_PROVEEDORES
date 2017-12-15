package com.vates.facpro.business.states;

public class Rechazada extends Estado{
	public static final Long STATE_ID = Estados.RECHAZADA;
	public static Estado estado;
	
	public static Estado getInstancia(){
		if(estado==null){
			estado = new Rechazada();
		}
		return estado;
	}


	@Override
	public Estado getProximoEstado() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getEstadoId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validarEstado() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String getNombre() {
		// TODO Auto-generated method stub
		return "Rechazada";
	}
	

}
