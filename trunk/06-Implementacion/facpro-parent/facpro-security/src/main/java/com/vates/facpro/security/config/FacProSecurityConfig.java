package com.vates.facpro.security.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Security layer's configuration.
 * 
 * @author Gaston Napoli
 * 
 */
@Configuration
@ImportResource("classpath:META-INF/facpro/security-config/cache-context.xml")
public class FacProSecurityConfig {

    @Bean(name = "security.publicUris")
    Set<String> publicUris() {
        Set<String> publicUris = new HashSet<String>();
        publicUris.add("/renew/*");
        publicUris.add("/login/login");
        publicUris.add("/login/reset");
        //publicUris.add("/*");
        return publicUris;

    }
    
}
