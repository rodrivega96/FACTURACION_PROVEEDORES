package com.vates.facpro.persistence.comparators;

import java.util.Comparator;

import com.vates.facpro.persistence.domain.HistorialAutorizacion;

public class HistorialComparator implements Comparator<HistorialAutorizacion> {

	@Override
	public int compare(HistorialAutorizacion hist1, HistorialAutorizacion hist2) {
		return hist1.getNivel().getOrden()
				.compareTo(hist2.getNivel().getOrden());
	}

}
