package com.vates.facpro.persistence.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Entity representing a system's user.
 * 
 * @author Cabrera Manuel
 * 
 */
@Data
public class UserView implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2560154996279210812L;
	private Long id;
	private Integer active;
	private String name;
	private String lastName;
	private String mail;
	private String job;
	private Long tipo;
	private Integer version;
	private String loginName;
	private String password;
	
	public static UserView loadUserView(Object[] user){
		UserView uv = new UserView();
		uv.setActive((Integer) user[0]);
		uv.setId((Long) user[1]);
		uv.setJob((String) user[2]);
		uv.setLastName((String) user[3]);
		uv.setMail((String) user[4]);
		uv.setName((String) user[5]);
		uv.setTipo((Long) user[6]);
		uv.setVersion((Integer) user[7]);
		return uv;
	}
	
	public static UserView loadCompleteUserView(Object[] user){
		UserView uv = loadUserView(user);
		uv.setPassword((String) user[8]);
		return uv;
	}
	
	
	
	public static List<UserView> loadUserViewList(List<Object[]> users){
		List<UserView> userResult = new ArrayList<UserView>();
		for(Object[] user : users){
			userResult.add(loadUserView(user));
		}
		return userResult;
	}
	
	public static List<UserView> loadCompleteUserViewList(List<Object[]> users){
		List<UserView> userResult = new ArrayList<UserView>();
		for(Object[] user : users){
			userResult.add(loadCompleteUserView(user));
		}
		return userResult;
	}

}
