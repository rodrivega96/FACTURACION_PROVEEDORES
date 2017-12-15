package com.vates.facpro.security.impl;

import javax.annotation.Resource;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

import org.apache.cxf.interceptor.security.AccessDeniedException;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.vates.facpro.persistence.domain.User;
import com.vates.facpro.persistence.service.UserService;
import com.vates.facpro.security.dto.UserDTO;
import com.vates.eng.ha.domain.impl.BasicCredential;
import com.vates.eng.ha.domain.impl.BasicUserToken;
import com.vates.eng.ha.generator.IdGenerator;
import com.vates.eng.ha.security.Authenticator;
import com.vates.eng.ha.util.MessageResourceBundle;

/**
 * Implementation for Authenticator.
 * 
 * @author Gaston Napoli
 * 
 */
@Service("facProAuthenticator")
@Slf4j
public class FacProAuthenticator implements Authenticator<BasicUserToken> {

	@Resource(name = "security.sessionIdGenerator.${generatorType:atomic}")
	private IdGenerator<String> idGenerator;

	@Inject
	private MessageResourceBundle messageResource;

	@Inject
	private CacheManager cacheManager;

	@Inject
	private UserService userService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vates.eng.ha.security.Authenticator#authenticate(com.vates.eng.ha
	 * .domain.UserToken)
	 */
	@Override
	public BasicCredential authenticate(BasicUserToken userToken) {

		log.debug("Authenticating ...");

		log.debug("Authenticating user {0} - password {1}", new Object[] {
				userToken.getLoginName(), userToken.getPassword() });

		if (!userToken.isValid()) {

			throw new AccessDeniedException(
					messageResource.getMessage("error.invalid.userToken"));

		}

		User user = userService.find(userToken.getLoginName());

		if (user == null) {

			throw new AccessDeniedException(
					messageResource.getMessage("error.unauthorized.access"));

		}

		// Generates a new credential with a given session ID.
		BasicCredential credential = BasicCredential
				.of(idGenerator.getNextId());

		// Stores in cache the user info associated with the session ID.
		cacheManager.getCache("defaultCache").put(credential.getSessionId(),
				UserDTO.of(user));

		return credential;

	}
	
	public BasicCredential authenticate(User user) {

		if (user == null) {

			throw new AccessDeniedException(
					messageResource.getMessage("error.unauthorized.access"));

		}

		// Generates a new credential with a given session ID.
		BasicCredential credential = BasicCredential
				.of(idGenerator.getNextId());

		// Stores in cache the user info associated with the session ID.
		cacheManager.getCache("defaultCache").put(credential.getSessionId(),
				UserDTO.of(user));

		return credential;

	}

	public User getUser(String login) {
		User user = userService.find(login);
		if (user == null) {
			throw new AccessDeniedException(
					messageResource.getMessage("error.unauthorized.access"));
		}
		return user;
	}

	public User getUser(String login, String password) {
		User user = userService.find(login, password);
		if (user == null) {
			throw new AccessDeniedException(
					messageResource.getMessage("error.unauthorized.access"));
		} else { // Por defecto, las búsquedas de MySQL no tienen sensibilidad a
					// mayúsculas
			if (user.getPassword().equals(password)) {
				return user;
			} else {
				throw new AccessDeniedException(
						messageResource.getMessage("error.unauthorized.access"));
			}
		}
	}

}
