package com.vates.facpro.persistence.comparators;

import java.util.Comparator;

import com.vates.facpro.persistence.domain.OptionMenuView;

public class OptionMenuViewComparator implements Comparator<OptionMenuView> {

    @Override
    public int compare(OptionMenuView res1, OptionMenuView res2) {
        if (res1 == null || res1.getOrder() == null) {
            return -1;
        } else {
            return res1.getOrder().compareTo(res2.getOrder());
        }
    }
    
}
