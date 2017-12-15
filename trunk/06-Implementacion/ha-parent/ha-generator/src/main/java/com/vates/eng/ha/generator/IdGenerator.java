package com.vates.eng.ha.generator;

/**
 * Genera IDs unicos.
 * 
 * @param <T>
 *            Tipo del ID generado
 */
public interface IdGenerator<T> {

    /**
     * Genera un ID unico.
     * 
     * @return el 'id' generado
     */
    public T getNextId();

}
