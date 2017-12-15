package com.vates.facpro.persistence.service.impl;

import javax.inject.Inject;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.config.LDAPUtil;
import com.vates.facpro.persistence.ldap.LdapHelper;
import com.vates.facpro.persistence.service.LDAPService;

/**
 * @author
 * 
 */
@Repository("LDAPService")
@Transactional(propagation = Propagation.NESTED)
public class LDAPServiceImpl implements LDAPService {
	
	@Inject
	private LDAPUtil ldapUtil;
	
	@SuppressWarnings("finally")
	@Override
	public String authenticatorUser(String user, String password) {
		String result = null;
		LdapHelper helper = ldapUtil.createHelper();
		try {
			Attributes att = helper.login(user, password);
			result = att.get("mail").toString();
			int start = result.indexOf(" ") + 1;
			int finish = result.indexOf("@");
			result = result.substring(start, finish);
		} catch (NamingException e) {
			e.printStackTrace();
		} finally {
			return result;
		}
	}
}