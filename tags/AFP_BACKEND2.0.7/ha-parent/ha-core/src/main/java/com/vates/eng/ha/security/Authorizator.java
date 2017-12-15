package com.vates.eng.ha.security;

import com.vates.eng.ha.domain.Credential;
import com.vates.eng.ha.domain.Operation;
import com.vates.eng.ha.domain.Resource;

/**
 * The Interface Authorizator.
 */
public interface Authorizator {

    /**
     * Autoriza a un usuario a la realizacion de una operacion sobre un recurso especifico.
     * 
     * @param credential
     *            derenciale the user token
     * @param resource
     *            the resource
     * @param operation
     *            the operation
     * @return true, if successful
     */
    public boolean isAuthorized(Credential credential, Resource resource, Operation operation);

    /**
     * Autoriza a un usuario a la realizacion de una operacion sobre un recurso especifico.
     * 
     * @param sessionId
     *            the session id
     * @param resource
     *            the resource
     * @param operation
     *            the operation
     * @return true, if successful
     */
    public boolean isAuthorized(String sessionId, Resource resource, Operation operation);

}
