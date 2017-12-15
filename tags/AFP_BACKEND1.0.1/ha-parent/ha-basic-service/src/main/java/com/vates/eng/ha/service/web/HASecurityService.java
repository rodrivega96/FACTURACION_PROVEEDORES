package com.vates.eng.ha.service.web;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import lombok.extern.slf4j.Slf4j;

import org.apache.cxf.interceptor.security.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.vates.eng.ha.domain.Credential;
import com.vates.eng.ha.domain.impl.RealmUserToken;
import com.vates.eng.ha.security.Authenticator;
import com.vates.eng.ha.util.MessageResourceBundle;

/**
 * @author Gaston Napoli
 * 
 */
@Service("security.service")
@Path("/security")
@Slf4j
public class HASecurityService {

    @Resource
    private Authenticator<RealmUserToken> authenticator;

    @Inject
    private MessageResourceBundle messageResource;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/form-login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response formLogin(@FormParam("loginName") String loginName, @FormParam("password") String password, @FormParam("realm") String realm) {

        return this.validateLoginInfo(new RealmUserToken(loginName, password, realm));
        
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(final RealmUserToken userToken) {

        return this.validateLoginInfo(userToken);
        
    }
    
    
    private Response validateLoginInfo(RealmUserToken userToken) {
        
        try {

            log.debug("loginName: " + userToken.getLoginName());

            log.debug("password: " + userToken.getPassword());

            log.debug("realm: " + userToken.getRealm());

            Credential credential = this.authenticator.authenticate(userToken);

            return Response.ok().entity(credential).build();

        } catch (AccessDeniedException ade) {

            log.error(this.messageResource.getMessage("error.unauthorized.access"), ade);

            return Response.status(Response.Status.FORBIDDEN).build();

        } catch (RuntimeException re) {

            log.error(this.messageResource.getMessage("error.login.exception"), re);

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        }
        
    }

}
