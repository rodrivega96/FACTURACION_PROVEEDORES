package com.vates.eng.ha.security.impl;

import org.apache.cxf.message.Message;
import org.springframework.stereotype.Service;

import com.vates.eng.ha.domain.impl.BasicOperation;
import com.vates.eng.ha.reader.OperationReader;

/**
 * Clase BasicOperationReader.
 */
@Service
public class BasicOperationReader extends OperationReader {

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.eng.ha.security.TokenReader#getToken(java.lang.Object)
     */
    @Override
    public BasicOperation getToken(Message value) {

        return new BasicOperation((String) value.get(Message.HTTP_REQUEST_METHOD));

    }

}
