package com.vates.eng.ha.exception;

import org.apache.cxf.interceptor.security.AccessDeniedException;

/**
 * @author Gaston Napoli
 *
 */
public class UserNotAuthenticatedException extends AccessDeniedException {

    private static final long serialVersionUID = -50285733296018841L;

    /**
     * @param reason
     */
    public UserNotAuthenticatedException(String reason) {
        
        super(reason);
        
    }

}
