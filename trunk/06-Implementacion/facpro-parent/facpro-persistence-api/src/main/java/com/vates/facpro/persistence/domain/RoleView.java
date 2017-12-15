package com.vates.facpro.persistence.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Entity representing a system's Role.
 * 
 * @author Cabrera Manuel
 * 
 */
@Data
public class RoleView implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 6733047684990973878L;
	private String modulo;
	private String ResourceRepresentation;
	private String permissionRepresentationPage;
	private String premisionReprensetation;
	private Long modOrd;
	private Long resOrd;
	private Long repOrd;
	
	
	public static RoleView loadRoleView(Object[] role){
		RoleView rv = new RoleView();
		rv.setModulo((String)role[0]);
		rv.setResourceRepresentation((String)role[1]);
		rv.setPermissionRepresentationPage((String)role[2]);
		rv.setPremisionReprensetation((String)role[3]);
		return rv;
	}
	
	public static RoleView loadCompleteRoleView(Object[] role){
		RoleView rv = loadRoleView(role);
		rv.setRepOrd((Long)role[4]);
		rv.setResOrd((Long)role[5]);
		rv.setModOrd((Long)role[6]);
		return rv;
	}
	
	
	public static List<RoleView> loadRoleViewList(List<Object[]> roles){
		List<RoleView> result = new ArrayList<RoleView>();
		for(Object[] role : roles){
			result.add(loadRoleView(role));
		}
		return result;
	}
	
	public static List<RoleView> loadCompleteRoleViewList(List<Object[]> roles){
		List<RoleView> result = new ArrayList<RoleView>();
		for(Object[] role : roles){
			result.add(loadCompleteRoleView(role));
		}
		return result;
	}

}
