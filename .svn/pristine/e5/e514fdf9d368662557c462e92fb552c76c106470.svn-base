package com.vates.facpro.business.levels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vates.facpro.business.states.EstadosNivel;
import com.vates.facpro.persistence.comparators.NivelesComparator;
import com.vates.facpro.persistence.domain.Factura;
import com.vates.facpro.persistence.domain.Nivel;
import com.vates.facpro.persistence.domain.NivelPublicado;
import com.vates.facpro.persistence.domain.User;

public class AdministradorNivelesAut {

	public void autorizar(Factura factura, User usuario) {
		List<Nivel> niv = getNivelesRestantes(factura);
		niv.get(0).setEstado(EstadosNivel.AUTORIZADO);
		if (niv.size() > 1) {
			factura.setNivel(niv.get(1));
		} else {
			factura.setNivel(null);
		}
	}

	public void autorizarSuperior(Factura factura, User usuario, List<Nivel> niv) {
		niv.get(0).setEstado(EstadosNivel.AUTORIZADO);
		if (niv.size() > 1) {
			factura.setNivel(niv.get(1));
		} else {
			factura.setNivel(null);
		}
	}

	public void rechazar(Factura factura, User usuario, List<Nivel> niv) {
		niv.get(0).setEstado(EstadosNivel.RECHAZADO);
		if (niv.size() > 1) {
			factura.setNivel(niv.get(1));
		} else {
			factura.setNivel(null);
		}
	}

	public void reiniciar(Factura factura, User usuario, String motivo) {

	}

	public List<Nivel> getNivelesRestantes(Factura factura) {
		Set<Nivel> auxNivel = new HashSet<Nivel>();
		for (NivelPublicado nvlPublic : factura.getNivelesPublicados()) {
			if (nvlPublic.getLast()) {
				auxNivel.addAll(nvlPublic.getNiveles());
			}
		}
		List<Nivel> listNivel = new ArrayList<Nivel>();
		listNivel.addAll(auxNivel);
		for (Nivel nvl : factura.getNiveles()) {
			if (nvl.getEstado() == EstadosNivel.AUTORIZADO
					|| nvl.getEstado() == EstadosNivel.RECHAZADO
					|| nvl.getEstado() == EstadosNivel.OBSERVADO) {
				listNivel.remove(nvl);
			}
		}
		Collections.sort(listNivel, new NivelesComparator());
		return listNivel;
	}

}
