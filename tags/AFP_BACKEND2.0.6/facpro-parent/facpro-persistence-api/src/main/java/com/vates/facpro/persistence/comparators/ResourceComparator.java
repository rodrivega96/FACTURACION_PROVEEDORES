package com.vates.facpro.persistence.comparators;

import java.util.Comparator;

import com.vates.facpro.persistence.domain.Resource;

public class ResourceComparator implements Comparator<Resource> {

    @Override
    public int compare(Resource res1, Resource res2) {
        if (res1 == null || res1.getOrd() == null) {
            return 1;
        } else {
            return res1.getOrd().compareTo(res2.getOrd());
        }
    }
    
}
