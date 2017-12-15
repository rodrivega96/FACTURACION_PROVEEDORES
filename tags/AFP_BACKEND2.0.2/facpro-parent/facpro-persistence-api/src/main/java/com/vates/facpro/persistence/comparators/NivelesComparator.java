package com.vates.facpro.persistence.comparators;

import java.util.Comparator;

import com.vates.facpro.persistence.domain.Nivel;

public class NivelesComparator implements Comparator<Nivel> {
	
	@Override
	public int compare(Nivel n1, Nivel n2) {
		return n1.getOrden().compareTo(n2.getOrden());
	}
}
