package com.vates.facpro.persistence.config;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.vates.facpro.persistence.ldap.LdapConfig;
import com.vates.facpro.persistence.ldap.LdapHelper;

@Configuration
@ComponentScan("com.vates.facpro.persistence") 
public class LDAPUtil {

	@Value("${ldap.domain}")
    private String domain;
	
    @Value("${ldap.host}")
    private String host;

    @Value("${ldap.port}")
    private String port;
    
    @Value("${ldap.pass}")
    private String password;
    
    @Value("${ldap.user}")
    private String username;
    
    @Value("${ldap.vates}")
    private String vates;
    
    
    @Value("${ldap.ssl}")
    private String ssl;
    
    @Bean
    public LdapHelper createHelper() {
    	LdapHelper helper = null;
		LdapConfig ldapConfig = new LdapConfig();
		ldapConfig.setDomain(domain);
		ldapConfig.setHost(host);
		ldapConfig.setPort(port);
		ldapConfig.setUserName(username);
		ldapConfig.setPassword(password);
		ldapConfig.setSsl(new Boolean(ssl));
		helper = new LdapHelper(ldapConfig, vates);
		try {
			helper.getInitialContext();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return helper;
    }
}