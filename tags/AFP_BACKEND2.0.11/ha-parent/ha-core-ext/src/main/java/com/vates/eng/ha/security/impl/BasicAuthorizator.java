package com.vates.eng.ha.security.impl;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Iterables.find;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.google.common.base.Predicate;
import com.vates.eng.ha.domain.Credential;
import com.vates.eng.ha.domain.Operation;
import com.vates.eng.ha.domain.Resource;
import com.vates.eng.ha.domain.impl.BasicCredential;
import com.vates.eng.ha.domain.impl.BasicOperation;
import com.vates.eng.ha.domain.impl.BasicResource;
import com.vates.eng.ha.dto.UserDTO;
import com.vates.eng.ha.security.Authorizator;
import com.vates.eng.ha.util.MessageResourceBundle;
import com.vates.eng.ha.util.impl.Utils;

/**
 * Implementacion por defecto de Authorizator.
 */
@Service
@Slf4j
public class BasicAuthorizator implements Authorizator {

    public final static String GRANT_FORMAT = "{0}-{1}";

    @Inject
    private CacheManager cacheManager;

    @Inject
    private MessageResourceBundle messageResource;

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.eng.ha.security.Authorizator#isAuthorized(com.vates.eng.ha.model.Credential, com.vates.eng.ha.model.Resource,
     * com.vates.eng.ha.model.Operation)
     */
    @Override
    public boolean isAuthorized(Credential credential, Resource resource, Operation operation) {

        return isAuthorized(((BasicCredential) credential).getSessionId(), resource, operation);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.eng.ha.security.Authorizator#isAuthorized(java.lang.String, com.vates.eng.ha.model.Resource, com.vates.eng.ha.model.Operation)
     */
    @Override
    public boolean isAuthorized(String sessionId, Resource resource, Operation operation) {

        if (isNullOrEmpty(sessionId) || resource == null || operation == null) {

            log.debug(messageResource.getMessage("error.null.argument"));

            log.debug("sessionId {}", sessionId);

            log.debug("resource {}", resource);

            log.debug("operation {}", operation);

            return false;
            
        }

        final String basicResource = ((BasicResource) resource).getName();
        final String basicOperation = ((BasicOperation) operation).getName();
        final String grantToBeValidated = Utils.basicGrantFormat(basicOperation, basicResource);

        log.debug("Trying to authorize sessionId: {} - resource: {} - operation {} - grant {} ", new Object[] { sessionId, basicResource,
                basicOperation, grantToBeValidated });

        ValueWrapper sessionData = this.cacheManager.getCache("defaultCache").get(sessionId);

        log.debug("Authorized sessionId {} - {}", sessionId, sessionData != null);

        // If no data was found in cache, that is considered as not logged.
        if (sessionData != null) {

            String grant = find(((UserDTO) sessionData.get()).getGrants(), new Predicate<String>() {

                @Override
                public boolean apply(final String input) {

                    log.debug("Searching grant {}, found {}", grantToBeValidated, input);

                    return input.equals(grantToBeValidated);

                }

            }, null);

            return grant != null;
        }

        return false;

    }
}
