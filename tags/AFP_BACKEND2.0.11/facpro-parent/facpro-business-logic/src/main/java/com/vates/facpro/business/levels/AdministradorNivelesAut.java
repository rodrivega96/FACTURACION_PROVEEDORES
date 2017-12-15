package com.vates.facpro.business.levels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vates.facpro.business.states.factura.EstadosNivel;
import com.vates.facpro.persistence.comparators.NivelesComparator;
import com.vates.facpro.persistence.domain.Factura;
import com.vates.facpro.persistence.domain.HistorialAutorizacion;
import com.vates.facpro.persistence.domain.Nivel;
import com.vates.facpro.persistence.domain.NivelPublicado;
import com.vates.facpro.persistence.domain.User;

public class AdministradorNivelesAut {

	public HistorialAutorizacion autorizar(Factura factura, Long userId,
			List<Nivel> niveles, NivelPublicado nivelPublicadoLast,
			Long estado, String motivo) {
		List<Nivel> niv = getNivelesRestantes(factura, niveles,
				nivelPublicadoLast);
		HistorialAutorizacion his = null;
		if (niv != null && !niv.isEmpty()
				&& userId.equals(niv.get(0).getUsuarioId())) {
			his = new HistorialAutorizacion();
			his.setEstado(estado);
			his.setDescripcion(motivo);
			his.setFactura(factura);
			his.setNivel(factura.getNivel());
			his.setUserId(userId);
			niv.get(0).setEstado(EstadosNivel.AUTORIZADO);
			if (niv.size() > 1) {
				factura.setNivel(niv.get(1));
			} else {
				factura.setNivel(null);
			}
		}
		return his;
	}

	public boolean autorizarSuperior(Factura fac, Long userId, List<Nivel> niveles, Long estado, String motivo,
			NivelPublicado nivelPublicadoLast, List<HistorialAutorizacion> listHistorial ) {
		
		List<Nivel> listNvl = getNivelesRestantes(fac, niveles, nivelPublicadoLast);
		for (Nivel nvl : getNivelesRestantes(fac, niveles, nivelPublicadoLast)) {
			HistorialAutorizacion his = new HistorialAutorizacion();
			his.setFactura(fac);
			his.setNivel(nvl);
			listNvl.get(0).setEstado(EstadosNivel.AUTORIZADO);
			if (!nvl.getUsuarioId().equals(userId)) {
				his.setEstado(estado.equals(HistorialAutorizacion.AUTORIZADA) ? HistorialAutorizacion.AUTORIZADA_SUPERIOR
						: HistorialAutorizacion.OBSERVADA_SUPERIOR);
				his.setDescripcion(estado
						.equals(HistorialAutorizacion.AUTORIZADA) ? ""
						: HistorialAutorizacion.OBSERVADA_SUPER);
				listHistorial.add(his);
			} else {
				his.setEstado(estado);
				his.setDescripcion(motivo);
				listHistorial.add(his);
				if (listNvl.size() > 1) {
					fac.setNivel(listNvl.get(1));
				} else {
					fac.setNivel(null);
				}
				return true;
			}
			listNvl.remove(nvl);
		}
		return false;
	}
	
	
	public boolean rechazarSuperior(Factura fac, Long userId, List<Nivel> niveles, String motivo,
			NivelPublicado nivelPublicadoLast, List<HistorialAutorizacion> listHistorial ){
		List<Nivel> listNvl = getNivelesRestantes(fac,niveles, nivelPublicadoLast);
		for (Nivel nvl : getNivelesRestantes(fac, niveles, nivelPublicadoLast)) {
			HistorialAutorizacion his = new HistorialAutorizacion();
			fac.getNivel().setEstado(EstadosNivel.RECHAZADO);
			his.setFactura(fac);
			his.setNivel(nvl);
			listNvl.get(0).setEstado(EstadosNivel.RECHAZADO);
			if (!nvl.getUsuarioId().equals(userId)) {
				his.setEstado(HistorialAutorizacion.RECHAZADA_SUPERIOR);
				his.setDescripcion(HistorialAutorizacion.RECHAZADA_SUPER);
				listHistorial.add(his);
			} else {
				his.setEstado(HistorialAutorizacion.RECHAZADA);
				his.setDescripcion(motivo);
				listHistorial.add(his);
				if (listNvl.size() > 1) {
					fac.setNivel(listNvl.get(1));
				} else {
					fac.setNivel(null);
				}
				return true;
			}
			listNvl.remove(nvl);
		}
		return false;
	}
	
	
	

	public HistorialAutorizacion rechazar(Factura factura, Long userId,
			String motivo) {
		HistorialAutorizacion his = null;
		if (factura.getNivel() != null
				&& userId.equals(factura.getNivel().getUsuarioId())) {
			his = new HistorialAutorizacion();
			his.setEstado(HistorialAutorizacion.RECHAZADA);
			his.setDescripcion(motivo);
			his.setFactura(factura);
			factura.getNivel().setEstado(EstadosNivel.RECHAZADO);
			his.setNivel(factura.getNivel());
			factura.setNivel(null);
		}
		return his;
	}

	public void reiniciar(Factura factura, User usuario, String motivo) {

	}

	public List<Nivel> getNivelesRestantes(Factura factura,
			List<Nivel> niveles, NivelPublicado nivelPublicadoLast) {
		Set<Nivel> auxNivel = new HashSet<Nivel>();
		auxNivel.addAll(nivelPublicadoLast.getNiveles());
		List<Nivel> listNivel = new ArrayList<Nivel>();
		listNivel.addAll(auxNivel);
		for (Nivel nvl : niveles) {
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
