package com.vates.facpro.persistence.comparators;

import java.util.Comparator;

import com.vates.facpro.persistence.domain.PermissionRepresentation;

public class PermissionRepresentationComparator implements Comparator<PermissionRepresentation> {

    @Override
    public int compare(PermissionRepresentation peRep1, PermissionRepresentation peRep2) {
        if (peRep1 == null || peRep1.getOrd() == null) {
            return 1;
        } else {
            return peRep1.getOrd().compareTo(peRep2.getOrd());
        }
    }
    
}
