package com.vates.eng.ha.reader.impl;

import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.message.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.vates.eng.ha.domain.impl.BasicCredential;
import com.vates.eng.ha.reader.CredentialReader;

/**
 * @author Gaston Napoli
 * 
 */
@Service
@Slf4j
public class BasicCredentialReader extends CredentialReader {

    @Value("${session.id.descriptor:ha-session-id}")
    private String sessionIdDescriptor;

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.eng.ha.reader.TokenReader#getToken(java.lang.Object)
     */
    @Override
    public BasicCredential getToken(Message message) {

        log.debug("Session ID descriptor: {}", sessionIdDescriptor);

        Map<String, List<String>> headers = CastUtils.cast((Map<?, ?>) message.get(Message.PROTOCOL_HEADERS));

        List<String> sessionIdWrapper = headers.get(sessionIdDescriptor);
        
        String sessionId = sessionIdWrapper != null ? sessionIdWrapper.get(0) : null;
        
        log.debug("Session ID could be read: {}", sessionId != null);

        BasicCredential credential = BasicCredential.of(sessionId);

        log.debug("Credential: {}", credential);

        return credential;

    }

    /**
     * @param sessionIdDescriptor
     */
    public void setSessionIdDescriptor(String sessionIdDescriptor) {
       
        this.sessionIdDescriptor = sessionIdDescriptor;
    
    }

}
