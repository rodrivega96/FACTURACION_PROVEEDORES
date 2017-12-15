package com.vates.eng.ha.reader;

import com.vates.eng.ha.domain.Resource;

/**
 * @author Gaston Napoli
 *
 */
public abstract class ResourceReader extends MessageTokenReader<Resource> {

    protected ResourceReader() {
        
        super(MessageTokenDescriptor.RESOURCE);
        
    }

}
