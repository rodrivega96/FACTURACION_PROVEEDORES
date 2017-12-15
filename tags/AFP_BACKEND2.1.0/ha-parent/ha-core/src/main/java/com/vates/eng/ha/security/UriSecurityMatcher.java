package com.vates.eng.ha.security;

/**
 * The Interface UriSecurityMatcher.
 */
public interface UriSecurityMatcher {

    /**
     * Evalua si una URI es publica.
     * 
     * @param uri
     *            la URI a evaluar
     * @return true, si es publical
     */
    public boolean isPublic(String uri);

}
