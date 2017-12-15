package com.vates.facpro.security.impl;

import org.apache.cxf.message.Message;
import org.springframework.stereotype.Service;

import com.vates.eng.ha.domain.Operation;
import com.vates.eng.ha.domain.impl.BasicOperation;
import com.vates.eng.ha.reader.OperationReader;

/**
 * Implements an operation reader which extracts the REST operation from the request data.
 * 
 * @author Gaston Napoli
 * 
 */
@Service
public class FacProOperationReader extends OperationReader {

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.eng.ha.reader.TokenReader#getToken(java.lang.Object)
     */
    @Override
    public Operation getToken(Message value) {

        return new BasicOperation((String) value.get(Message.HTTP_REQUEST_METHOD));

    }

}
