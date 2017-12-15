package com.vates.facpro.service.web.impl;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.interceptor.security.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.vates.eng.ha.domain.impl.BasicCredential;
import com.vates.eng.ha.domain.impl.BasicUserToken;
import com.vates.facpro.persistence.domain.User;
import com.vates.facpro.persistence.mail.MailUtil;
import com.vates.facpro.persistence.service.LDAPService;
import com.vates.facpro.persistence.service.UserService;
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
	private static final int STATUS_MAIL_EXCEPTION = 3;
	private static final int STATUS_INVALID_DATA = 4;

	@Inject
	private FacProMenuLoader facProMenuLoader;

	@Inject
	private FacProAuthenticator facProAuthenticator;

	@Inject
	private MailUtil mailUtil;

	@Inject
	private UserService userService;

	@Inject
	private LDAPService ldapService;

	@SuppressWarnings("finally")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/reset")
	@Consumes(MediaType.APPLICATION_JSON)
	public LoginResponseDTO reset(LoginDTO loginDTO) {
		LoginResponseDTO response = new LoginResponseDTO();
		User user = new User();
		try {
			user = userService.find(loginDTO.getUserName());
			if (null != user) {
				// user.setLogin(loginDTO.getUserName());
				user = userService.resetPass(user);
				userService.update(user);

				mailUtil.javaMailService();
				mailUtil.sendMessage(loginDTO.getUserMail(),
						"Datos de usuario",
						"<b>Sus nuevos datos para autenticarse son</b>:<br><br>Usuario: "
								+ loginDTO.getUserName() + "<br>Contraseña: "
								+ user.getPassword());
			} else {
				response.setMessage("");
				response.setStatus(STATUS_INVALID_DATA);
			}

		} catch (MessagingException ex) {
			response.setMessage("No se pudo enviar e-mail al estudiante");
			response.setStatus(STATUS_MAIL_EXCEPTION);
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(STATUS_DATABASE_EXCEPTION);
		} finally {
			return response;
		}

	}

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
			BasicUserToken token = new BasicUserToken(loginDTO.getUserName(),
					loginDTO.getUserPassword());
			BasicCredential crd = facProAuthenticator.authenticate(token);
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
