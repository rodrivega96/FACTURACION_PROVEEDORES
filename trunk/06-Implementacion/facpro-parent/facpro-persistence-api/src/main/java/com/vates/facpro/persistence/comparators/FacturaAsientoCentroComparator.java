package com.vates.facpro.persistence.comparators;

import java.util.Comparator;

import com.vates.facpro.persistence.domain.FacturaAsientoAdm;

public class FacturaAsientoCentroComparator implements Comparator<FacturaAsientoAdm> {

	@Override
	public int compare(FacturaAsientoAdm as1, FacturaAsientoAdm as2) {
		if (as1.getNroCentroCostos() == null) {
			return 1;
		}
		if (as2.getNroCentroCostos() == null) {
			return -1;
		}
		return -(as1.getNroCentroCostos().compareTo(as2.getNroCentroCostos()));
	}

}
