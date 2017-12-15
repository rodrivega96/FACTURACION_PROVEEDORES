package com.vates.eng.ha.generator;

/**
 * It defines the interface for unique ID generators.
 * 
 * @param <T>
 *            Generated ID's datatype
 */
public interface IdGenerator<T> {

    /**
     * It returns an unique ID.
     * 
     * @return the generated ID.
     */
    public T getNextId();

}
