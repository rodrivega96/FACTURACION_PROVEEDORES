package com.vates.facpro.security.impl;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Iterables.find;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.CacheManager;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.stereotype.Service;

import com.google.common.base.Predicate;
import com.vates.facpro.security.dto.UserDTO;
import com.vates.eng.ha.domain.Credential;
import com.vates.eng.ha.domain.Operation;
import com.vates.eng.ha.domain.Resource;
import com.vates.eng.ha.domain.impl.BasicCredential;
import com.vates.eng.ha.domain.impl.BasicOperation;
import com.vates.eng.ha.domain.impl.BasicResource;
import com.vates.eng.ha.security.Authorizator;
import com.vates.eng.ha.util.MessageResourceBundle;
import com.vates.eng.ha.util.impl.Utils;

/**
 * Implementation for Authorizator.
 * 
 * @author Gaston Napoli
 * 
 */
@Service
@Slf4j
public class FacProAuthorizator implements Authorizator {

    @Inject
    private CacheManager cacheManager;

    @Inject
    private MessageResourceBundle messageResource;

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.eng.ha.security.Authorizator#isAuthorized(com.vates.eng.ha.domain.Credential, com.vates.eng.ha.domain.Resource,
     * com.vates.eng.ha.domain.Operation)
     */
    @Override
    public boolean isAuthorized(Credential credential, Resource resource, Operation operation) {

        return isAuthorized(((BasicCredential) credential).getSessionId(), resource, operation);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.eng.ha.security.Authorizator#isAuthorized(java.lang.String, com.vates.eng.ha.domain.Resource, com.vates.eng.ha.domain.Operation)
     */
    @Override
    public boolean isAuthorized(String sessionId, Resource resource, Operation operation) {

        // All params are mandatory
        if (isNullOrEmpty(sessionId) || resource == null || operation == null) {

            log.error(messageResource.getMessage("error.null.argument"));

            log.debug("sessionId {} - resource {} - operation {}", sessionId, resource, operation);

            return false;

        }

        final String requestedResource = ((BasicResource) resource).getName();

        final String requestedOperation = ((BasicOperation) operation).getName();

        // It uses default grant format ({operation}_{resource})
        final String grantToBeValidated = Utils.basicGrantFormat(requestedOperation, requestedResource);

        log.debug("Trying to authorize sessionId: {} - resource: {} - operation {} - grant {} ", new Object[] { sessionId, requestedResource,
                requestedOperation, grantToBeValidated });

        // It looks for session related data in the cache.
        ValueWrapper sessionData = null;
        synchronized("ACCESS"){
          sessionData = this.cacheManager.getCache("defaultCache").get(sessionId);
        }

        log.debug("Authorized sessionId {} - {}", sessionId, sessionData != null);

        // If no data was found in cache, that is considered as not logged.
        if (sessionData != null) {
        	
        	// Re-insert data in the cache in order to refresh expiration time.
//        	this.cacheManager.getCache("defaultCache").put(sessionId, sessionData);

            // It extracts from session data the user specific info.
            String grant = null;
            synchronized("ACCESS"){
                UserDTO userDto = (UserDTO) sessionData.get();
                this.cacheManager.getCache("defaultCache").evict(sessionId);
                this.cacheManager.getCache("defaultCache").put(sessionId, userDto);
                
                // It looks up if user has the corresponding grants in order to access to resource.
                grant = find(userDto.getGrants(), new Predicate<String>() {

                    @Override
                    public boolean apply(final String input) {

                        log.debug("Looking for grant {}, found {}", grantToBeValidated, input);

                        return input.equals(grantToBeValidated);

                    }

                }, null);
            }

            // If grant is found, user is authorized.
            return grant != null;

        }

        // User is not authorized.
        return false;

    }

}
