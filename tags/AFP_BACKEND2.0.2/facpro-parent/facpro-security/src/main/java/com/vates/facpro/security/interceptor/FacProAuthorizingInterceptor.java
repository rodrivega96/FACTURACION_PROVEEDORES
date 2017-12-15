package com.vates.facpro.security.interceptor;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.message.Message;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.vates.facpro.security.dto.UserDTO;
import com.vates.eng.ha.domain.Credential;
import com.vates.eng.ha.domain.Operation;
import com.vates.eng.ha.domain.Resource;
import com.vates.eng.ha.domain.impl.BasicCredential;
import com.vates.eng.ha.interceptor.AbstractAuthorizingInterceptor;
import com.vates.eng.ha.reader.CredentialReader;
import com.vates.eng.ha.reader.OperationReader;
import com.vates.eng.ha.reader.ResourceReader;
import com.vates.eng.ha.security.Authorizator;

/**
 * Web service requests interceptor. It is responsible for user authorization.
 * 
 * @author Gaston Napoli
 * 
 */
@Service
public class FacProAuthorizingInterceptor extends AbstractAuthorizingInterceptor {

    @Inject
    private Authorizator authorizator;

    @Inject
    private CredentialReader credentialReader;

    @Inject
    private ResourceReader resourceReader;

    @Inject
    private OperationReader operationReader;

    @Inject
    private CacheManager cacheManager;

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.eng.ha.interceptor.AbstractAuthorizingInterceptor#getCredential(org.apache.cxf.message.Message)
     */
    @Override
    protected Credential getCredential(Message message) {
        Credential cr = credentialReader.getToken(message);
        return  cr;

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

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.eng.ha.interceptor.AbstractAuthorizingInterceptor#postprocess(org.apache.cxf.message.Message)
     */
    @Override
    protected void postprocess(Message message) {

        BasicCredential credential = (BasicCredential) credentialReader.getToken(message);

        String sessionId = credential.getSessionId();

        if (sessionId != null) {

            ValueWrapper sessionData = this.cacheManager.getCache("defaultCache").get(sessionId);

            if (sessionData != null) {

                UserDTO user = (UserDTO) sessionData.get();

                Map<String, List<String>> headers = CastUtils.cast((Map<?, ?>) message.get(Message.PROTOCOL_HEADERS));
                if (headers != null) {
                    headers.put("user_name", Lists.newArrayList(user.getLoginName()));
                    headers.put("user_id", Lists.newArrayList(user.getId().toString()));
                }

            }

        }

    }
    
}
