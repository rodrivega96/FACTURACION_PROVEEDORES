package com.vates.eng.ha.security.impl;

import javax.annotation.Resource;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

import org.apache.cxf.interceptor.security.AccessDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.vates.eng.ha.generator.IdGenerator;
import com.vates.eng.ha.domain.impl.BasicCredential;
import com.vates.eng.ha.domain.impl.RealmUserToken;
import com.vates.eng.ha.dto.UserDTO;
import com.vates.eng.ha.persistence.domain.User;
import com.vates.eng.ha.persistence.service.UserService;
import com.vates.eng.ha.security.Authenticator;
import com.vates.eng.ha.util.MessageResourceBundle;

/**
 * @author Gaston Napoli
 * 
 */
@Service
@Slf4j
public class BasicAuthenticator implements Authenticator<RealmUserToken> {

    @Resource(name = "security.sessionIdGenerator.${generatorType:atomic}")
    private IdGenerator<String> idGenerator;

    @Inject
    private MessageResourceBundle messageResource;

    @Inject
    private CacheManager cacheManager;

    @Autowired(required = false)
    private UserService userService;

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.eng.ha.security.Authenticator#authenticate(com.vates.eng.ha.model.UserToken)
     */
    @Override
    public BasicCredential authenticate(RealmUserToken userToken) {

        log.debug("Authenticating ...");

        log.debug("Trying to authenticate user {0} - password {1} - realm {2}", new Object[] { userToken.getLoginName(), userToken.getPassword(),
                userToken.getRealm() });

        if (!userToken.isValid()) {

            throw new AccessDeniedException(messageResource.getMessage("error.invalid.userToken"));

        }

        User user = userService.find(userToken.getLoginName(), userToken.getPassword(), userToken.getRealm());

        if (user == null) {

            throw new AccessDeniedException(messageResource.getMessage("error.unauthorized.access"));

        }

        // Generates a new credential with a given session ID.
        BasicCredential credential = BasicCredential.of(idGenerator.getNextId());

        // Stores in cache the user info associated with the session ID.
        cacheManager.getCache("defaultCache").put(credential.getSessionId(), UserDTO.of(user));

        return credential;

    }

}
