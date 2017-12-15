package com.vates.eng.ha.generator.impl;

import java.util.concurrent.atomic.AtomicLong;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.vates.eng.ha.exception.MaximumSessionsReachedException;

/**
 * Clase PerNodeAtomicBasedIdGeneratorImpl.
 */
@Service("security.sessionIdGenerator.atomic")
@Slf4j
public class PerNodeAtomicBasedIdGeneratorImpl extends AbstractPerNodeBasedIdGenerator<String> {

    /** The incrementing value. */
    private final AtomicLong incrementingValue;

    /**
     * 
     */
    public PerNodeAtomicBasedIdGeneratorImpl() {

        super();
        incrementingValue = new AtomicLong(0);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.eng.ha.generator.IdGenerator#getNextId()
     */
    @Override
    public String getNextId() {

        incrementingValue.incrementAndGet();

        if (incrementingValue.compareAndSet(Long.MIN_VALUE, Long.MIN_VALUE) == true) {
            
            throw new MaximumSessionsReachedException("Cannot instantiate new session ids");
            
        }

        String generatedId = new StringBuilder(hostname).append(System.nanoTime()).append(incrementingValue.get()).toString();
        
        log.debug("Generated ID: " + generatedId);

        return generatedId;

    }
    
}
