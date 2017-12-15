package com.vates.eng.ha.security.impl;

import java.util.Set;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.util.PatternMatchUtils;

import com.vates.eng.ha.security.UriSecurityMatcher;

/**
 * BasicUriSecurityMatcher.
 */
@Service
@Slf4j
public class BasicUriSecurityMatcher implements UriSecurityMatcher {

    /**
     * Stores public resources, which are accessible out of security schema.
     */
    @Resource(name = "security.publicUris")
    private Set<String> publicUris;

    /**
     * Default constructor.
     */
    public BasicUriSecurityMatcher() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.eng.ha.security.UriSecurityMatcher#isPublic(java.lang.String)
     */
    @Override
    public boolean isPublic(String uri) {

        log.debug("\tURI to evaluate: " + uri);

        for (String uriPattern : publicUris) {

            log.debug("\tURI pattern evaluated: {}", uriPattern);

            if (PatternMatchUtils.simpleMatch(uriPattern, uri) == true) {

                log.debug("\t\tURI pattern match!!: {}", uriPattern);

                return true;

            }

        }

        return false;

    }

}
