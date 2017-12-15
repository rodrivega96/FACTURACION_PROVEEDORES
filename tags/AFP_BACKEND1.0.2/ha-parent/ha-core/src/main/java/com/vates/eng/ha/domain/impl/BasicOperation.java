package com.vates.eng.ha.domain.impl;

import com.vates.eng.ha.domain.Operation;



/**
 * @author Gaston Napoli
 *
 */
public class BasicOperation implements Operation {

    private String name;

    /**
     * Instantiates a new hA operation impl.
     *
     * @param name the name
     */
    public BasicOperation(String name) {
        
        this.name = name;
        
    }

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName() {
        
        return name;
        
    }

    /**
     * Sets the name.
     * 
     * @param name
     *            the new name
     */
    public void setName(String name) {
        
        this.name = name;
        
    }
    
}
