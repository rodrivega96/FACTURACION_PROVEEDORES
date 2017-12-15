package com.vates.eng.ha.reader;

import com.vates.eng.ha.domain.Token;

/**
 * @author Gaston Napoli
 *
 * @param <T>
 * @param <V>
 * @param <S>
 */
public interface TokenReader<T extends Token, V, S> {

    /**
     * 
     * @return
     */
    public S getDescriptor();

    /**
     * @param value
     * @return
     */
    public T getToken(V value);

}
