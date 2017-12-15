package com.vates.facpro.service.web.impl;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.interceptor.security.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.vates.eng.ha.domain.impl.BasicCredential;
import com.vates.facpro.persistence.domain.User;
import com.vates.facpro.persistence.service.LDAPService;
import com.vates.facpro.security.impl.FacProAuthenticator;
import com.vates.facpro.security.impl.FacProMenuLoader;
import com.vates.facpro.service.web.dto.LoginDTO;
import com.vates.facpro.service.web.dto.LoginResponseDTO;

@Service("facpro.service.login")
@Path("/login")
public class LoginWebService {

	private static final int STATUS_OK = 0;
	private static final int STATUS_DATABASE_EXCEPTION = 1;
	private static final int STATUS_INVALID_USER = 2;

	@Inject
	private FacProMenuLoader facProMenuLoader;

	@Inject
	private FacProAuthenticator facProAuthenticator;

	@Inject
	private LDAPService ldapService;

	@SuppressWarnings("finally")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	public LoginResponseDTO login(LoginDTO loginDTO) {
		LoginResponseDTO response = new LoginResponseDTO();
		response.setStatus(STATUS_OK);
		response.setMessage("");
		response.setToken("");
		response.setMenu(facProMenuLoader.generateInitialMenu());
		try {
			User user = null;
			if (loginDTO.getUserDomain().equals(User.DOMAIN_VATESSA)) {
				String result = ldapService.authenticatorUser(
						loginDTO.getUserName(), loginDTO.getUserPassword());
				if (result == null) {
					throw new AccessDeniedException("Ususario inactivo");
				}
				response.setUserName(result);
				user = facProAuthenticator.getUser(loginDTO.getUserName());
			} else {
				user = facProAuthenticator.getUser(loginDTO.getUserName(),
						loginDTO.getUserPassword());
				response.setUserName(loginDTO.getUserName());
			}
			if (user.getActive() == User.INACTIVE
					|| (!loginDTO.getUserDomain().equals(User.DOMAIN_VATESSA) && User.DOMAIN_VATESSA
							.equals(user.getTipo()))) {
				throw new AccessDeniedException("Ususario invalido");
			}
			BasicCredential crd = facProAuthenticator.authenticate(user);
			response.setToken(crd.getSessionId());
			response.setUserId(user.getId());
			response.setMenu(facProMenuLoader.generateMenu(user));
			response.setToken(crd.getSessionId());
			response.setUserId(user.getId());
			response.setStatus(STATUS_OK);
		} catch (AccessDeniedException e) {
			response.setStatus(STATUS_INVALID_USER);
			response.setMessage(e.getMessage());
		} catch (Exception e) {
			response.setStatus(STATUS_DATABASE_EXCEPTION);
			response.setMessage(e.getMessage());
		} finally {
			return response;
		}
	}

	@SuppressWarnings("finally")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getMenu")
	@Consumes(MediaType.APPLICATION_JSON)
	public LoginResponseDTO getMenu(LoginDTO loginDTO) {
		LoginResponseDTO response = new LoginResponseDTO();
		response.setStatus(STATUS_OK);
		response.setMessage("");
		response.setMenu(facProMenuLoader.generateInitialMenu());
		try {
			User user = facProAuthenticator.getUser(loginDTO.getUserName());
			response.setMenu(facProMenuLoader.generateMenu(user));
		} catch (AccessDeniedException e) {
			response.setStatus(STATUS_INVALID_USER);
			response.setMessage(e.getMessage());
		} catch (Exception e) {
			response.setStatus(STATUS_DATABASE_EXCEPTION);
			response.setMessage(e.getMessage());
		} finally {
			return response;
		}
	}

}
