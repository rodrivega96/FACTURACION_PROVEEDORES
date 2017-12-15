package com.vates.eng.ha.reader;

/**
 * @author Gaston Napoli
 * 
 */
public enum MessageTokenDescriptor {

    CREDENTIAL("credential"), OPERATION("credential"), RESOURCE("credential");

    private final String id;

    /**
     * @param id
     */
    MessageTokenDescriptor(String id) {

        this.id = id;

    }

    /**
     * @return
     */
    public String getId() {

        return this.id;

    }
    
}
