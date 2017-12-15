package com.vates.facpro.service.web.comparators;

import java.util.Comparator;

import com.vates.facpro.service.web.dto.ProyectoDTO;

public class ProyectoDTOComparator implements Comparator<ProyectoDTO> {

	@Override
	public int compare(ProyectoDTO p1, ProyectoDTO p2) {
		return p1.getExtendido() && p2.getExtendido() ? 1
				: p1.getExtendido() ? 1 : -1;
	}

}
