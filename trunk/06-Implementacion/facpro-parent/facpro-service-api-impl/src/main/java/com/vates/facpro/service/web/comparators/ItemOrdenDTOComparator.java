package com.vates.facpro.service.web.comparators;

import java.util.Comparator;

import com.vates.facpro.service.web.dto.ItemOrdenDTO;

public class ItemOrdenDTOComparator implements Comparator<ItemOrdenDTO> {

	@Override
	public int compare(ItemOrdenDTO io1, ItemOrdenDTO io2) {
		return io1.isExtendido() && io2.isExtendido() ? 1
				: io1.isExtendido() ? 1 : -1;
	}

}
