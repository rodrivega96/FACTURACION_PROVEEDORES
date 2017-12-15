package com.vates.facpro.business.states;

public class Autorizada extends Estado{
	public static final Long STATE_ID = Estados.AUTORIZADA;
	public static Estado estado;
	
	public static Estado getInstancia(){
		if(estado==null){
			estado = new Autorizada();
		}
		return estado;
	}

	@Override
	public Estado getProximoEstado() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean validarEstado() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Long getEstadoId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNombre() {
		// TODO Auto-generated method stub
		return "Autorizada";
	}
}
