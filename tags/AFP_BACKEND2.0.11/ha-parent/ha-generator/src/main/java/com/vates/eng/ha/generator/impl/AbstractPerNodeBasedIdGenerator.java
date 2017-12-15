package com.vates.eng.ha.generator.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.vates.eng.ha.exception.IdGenerationException;
import com.vates.eng.ha.generator.IdGenerator;

/**
 * The Class AbstractPerNodeBasedIdGenerator.
 *
 * @param <T> the generic type
 */
public abstract class AbstractPerNodeBasedIdGenerator<T> implements IdGenerator<T> {

    /** The hostname. */
    protected String hostname;

    /**
     * Instantiates a new abstract per node based id generator.
     */
    public AbstractPerNodeBasedIdGenerator() {
        try {

            hostname = InetAddress.getLocalHost().getHostAddress().concat(":");

        } catch (UnknownHostException uhe) {
            throw new IdGenerationException("Error getting localhost address", uhe);
        }
    }

}