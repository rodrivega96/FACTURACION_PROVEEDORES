package com.vates.eng.ha.security;

import com.vates.eng.ha.domain.Credential;
import com.vates.eng.ha.domain.UserToken;

/**
 * Define el contrato para la jerarquia de autenticadores
 * 
 * @param <T>
 *            Tipo usado por el autenticador en el proceso de autenticacion
 */
public interface Authenticator<T extends UserToken> {

    /**
     * Autentica un usuario.
     * 
     * @param userToken
     *            informacion del usuario a autenticar
     * @return la credencial generada para el usuario autenticado
     * @throws UserNotAuthenticatedException
     *             si no puede autenticar al usuario
     */
    public Credential authenticate(T userToken);

}
