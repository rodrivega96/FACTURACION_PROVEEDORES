package com.vates.eng.ha.filter;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import lombok.extern.slf4j.Slf4j;

import org.apache.cxf.interceptor.security.AccessDeniedException;
import org.apache.cxf.jaxrs.ext.RequestHandler;
import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.message.Message;
import org.springframework.stereotype.Service;

import com.vates.eng.ha.interceptor.AbstractAuthorizingInterceptor;

/**
 * The Class HAAuthorizingFilter.
 */
@Service("security.authorizingFilter")
@Slf4j
public class HAAuthorizingFilter implements RequestHandler {

    @Inject
    private AbstractAuthorizingInterceptor interceptor;

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.cxf.jaxrs.ext.RequestHandler#handleRequest(org.apache.cxf.message.Message, org.apache.cxf.jaxrs.model.ClassResourceInfo)
     */
    @Override
    public Response handleRequest(Message message, ClassResourceInfo resourceClass) {

        try {

            interceptor.handleMessage(message);

            return null;

        } catch (AccessDeniedException ade) {

            log.error(null, ade);

            return Response.status(Response.Status.FORBIDDEN).build();

        } catch (RuntimeException re) {

            log.error(null, re);

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        }

    }

}
