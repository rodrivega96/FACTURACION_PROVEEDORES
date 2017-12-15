package com.vates.facpro.persistence.ldap;

import lombok.Data;

@Data
public class LdapConfig {

	
	private String userName;
	private String password;
	private String domain;
	private String host;
	private String keystore;
	private String kaystorePassword;
	private String port;
	private boolean ssl;

	public String getUrlString() {
		String url = "ldap://" + host + ":" + port;
		if(ssl){
			url = "ldaps://" + host + ":" + port;
		}
		return url;
	}

	public String getDomaindDCItems() {
		String dist = "";
		String[] dom = domain.split("\\.");
		for (int i = 0; i < dom.length; i++) {
			if (i > 0) {
				dist = dist + ",";
			}
			dist = dist + "DC=" + dom[i];
		}
		return dist;
	}

	public String getDistinguisInitalName() {
		return distinguisInitalName(userName);
	}

	public String distinguisInitalName(String cnValue) {

		String dist = "CN=" + cnValue + ",CN=Users," + getDomaindDCItems();
		return dist;
	}

}
