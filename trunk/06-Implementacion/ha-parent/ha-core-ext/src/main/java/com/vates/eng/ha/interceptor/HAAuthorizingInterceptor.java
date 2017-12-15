package com.vates.eng.ha.interceptor;

import javax.inject.Inject;

import org.apache.cxf.message.Message;
import org.springframework.stereotype.Service;

import com.vates.eng.ha.domain.Credential;
import com.vates.eng.ha.domain.Operation;
import com.vates.eng.ha.domain.Resource;
import com.vates.eng.ha.reader.CredentialReader;
import com.vates.eng.ha.reader.OperationReader;
import com.vates.eng.ha.reader.ResourceReader;
import com.vates.eng.ha.security.Authorizator;

/**
 * Interceptor CXF. Implementacion por defecto del framework, encargado de la gestion de seguridad.
 */
// @Service("ha.security.authorizing.interceptor")
@Service
public class HAAuthorizingInterceptor extends AbstractAuthorizingInterceptor {

    @Inject
    private Authorizator authorizator;

    @Inject
    private CredentialReader credentialReader;

    @Inject
    private ResourceReader resourceReader;

    @Inject
    private OperationReader operationReader;

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.eng.ha.interceptor.AbstractAuthorizingInterceptor#getCredential(org.apache.cxf.message.Message)
     */
    @Override
    protected Credential getCredential(Message message) {

        return credentialReader.getToken(message);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.eng.ha.interceptor.AbstractAuthorizingInterceptor#getOperation(org.apache.cxf.message.Message)
     */
    @Override
    protected Operation getOperation(Message message) {

        return operationReader.getToken(message);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.eng.ha.interceptor.AbstractAuthorizingInterceptor#getResource(org.apache.cxf.message.Message)
     */
    @Override
    protected Resource getResource(Message message) {

        return resourceReader.getToken(message);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.eng.ha.interceptor.AbstractAuthorizingInterceptor#isAuthorized(com.vates.eng.ha.domain.Credential,
     * com.vates.eng.ha.domain.Resource, com.vates.eng.ha.domain.Operation)
     */
    @Override
    protected boolean isAuthorized(Credential credential, Resource resource, Operation operation) {

        return authorizator.isAuthorized(credential, resource, operation);

    }

}
