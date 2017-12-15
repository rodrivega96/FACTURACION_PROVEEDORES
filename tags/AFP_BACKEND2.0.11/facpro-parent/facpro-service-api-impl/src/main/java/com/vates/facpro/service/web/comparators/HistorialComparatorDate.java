package com.vates.facpro.service.web.comparators;

import java.util.Comparator;

import com.vates.facpro.service.web.dto.HistorialWFDTO;

public class HistorialComparatorDate implements Comparator<HistorialWFDTO> {

	@Override
	public int compare(HistorialWFDTO hist1, HistorialWFDTO hist2) {
		if (hist1.getUpdate() == null && hist2.getUpdate() == null) {
			return hist1.getNivel().compareTo(hist2.getNivel());
		} else if (hist1.getUpdate() == null) {
			return 1;
		} else if (hist2.getUpdate() == null) {
			return -1;
		} else if (hist1.getUpdate().equals(hist2.getUpdate())) {
			return hist1.getNivel().compareTo(hist2.getNivel());
		}
		return hist1.getUpdate().compareTo(hist2.getUpdate());
	}

}
