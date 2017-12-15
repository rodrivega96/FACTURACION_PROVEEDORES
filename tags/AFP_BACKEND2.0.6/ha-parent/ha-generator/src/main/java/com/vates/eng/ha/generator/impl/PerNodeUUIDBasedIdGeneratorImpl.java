package com.vates.eng.ha.generator.impl;

import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

/**
 * Clase PerNodeUUIDBasedIdGeneratorImpl.
 */
@Service("security.sessionIdGenerator.uuid")
@Slf4j
public class PerNodeUUIDBasedIdGeneratorImpl extends AbstractPerNodeBasedIdGenerator<String> {

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.eng.ha.generator.IdGenerator#getNextId()
     */
    @Override
    public String getNextId() {

        String generatedId = new StringBuilder(hostname).append(UUID.randomUUID()).toString();
        
        log.debug("Generated ID: " + generatedId);

        return generatedId;

    }

}
