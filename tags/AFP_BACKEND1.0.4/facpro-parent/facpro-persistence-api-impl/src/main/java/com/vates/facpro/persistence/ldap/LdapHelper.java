package com.vates.facpro.persistence.ldap;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

public class LdapHelper {
	private LdapConfig config;
	@SuppressWarnings("unused")
	private String domain;
	private DirContext initial;

	public LdapHelper(LdapConfig config, String domain) {
		this.config = config;
		this.domain = domain;
	}

	public DirContext getInitialContext() throws NamingException {
			initial = getContext(config.getDistinguisInitalName(),
					config.getPassword());
		return initial;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private DirContext getContext(String cn, String password)
			throws NamingException {
		Hashtable env = new Hashtable();

		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.ldap.LdapCtxFactory");

		// set security credentials
		env.put(Context.SECURITY_PRINCIPAL, cn);
		env.put(Context.SECURITY_CREDENTIALS, password);
		if(config.isSsl()){
			env.put(Context.SECURITY_PROTOCOL, "SSL");
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put("java.naming.ldap.factory.socket",
					"com.vates.facpro.service.web.dto.PermissiveSSLSocketFactory");
		}

		// connect to my domain controller
		env.put(Context.PROVIDER_URL, config.getUrlString());
		// Create the initial directory context
		DirContext ctx = new InitialLdapContext(env, null);
		return ctx;

	}

	@SuppressWarnings("rawtypes")
	protected String getName(String userName) throws NamingException {

		String result = "";
		String[] as = { "distinguishedName" };
		NamingEnumeration answer = findUser(userName, as);

		while (answer.hasMoreElements()) {
			SearchResult sr = (SearchResult) answer.next();
			Attributes attrs = sr.getAttributes();
			result = (String) attrs.get("distinguishedName").get();
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	private NamingEnumeration findUser(String username, String[] attrs)
			throws NamingException {

		DirContext context = getInitialContext();

		SearchControls searchCtls = new SearchControls();

		if (attrs != null) {
			// Specify the attributes to return
			searchCtls.setReturningAttributes(attrs);
		}

		// Specify the search scope
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		// specify the LDAP search filter
		String searchFilter = "sAMAccountName=" + username;

		// Specify the Base for the search
		String searchBase = config.getDomaindDCItems();

		// initialize counter to total the results

		// Search for objects using the filter
		NamingEnumeration answer = context.search(searchBase, searchFilter,
				searchCtls);

		return answer;

	}

	public Attributes login(String user, String password)
			throws NamingException {
		String distinguishedName = "";
		distinguishedName = getName(user);
		if (distinguishedName == null || "".equals(distinguishedName)) {
			return null;
		}
		getContext(distinguishedName, password);
		Attributes attrs = initial.getAttributes(distinguishedName);
		return attrs;
	}
}
