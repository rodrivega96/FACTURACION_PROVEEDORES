package com.vates.facpro.persistence.comparators;

import java.util.Comparator;

import com.vates.facpro.persistence.domain.Module;

public class ModuleComparator implements Comparator<Module> {

    @Override
    public int compare(Module res1, Module res2) {
        if (res1 == null || res1.getOrd() == null) {
            return 1;
        } else {
            return res1.getOrd().compareTo(res2.getOrd());
        }
    }
    
}
