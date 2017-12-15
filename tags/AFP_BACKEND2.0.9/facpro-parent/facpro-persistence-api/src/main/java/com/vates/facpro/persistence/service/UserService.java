package com.vates.facpro.persistence.service;

import java.util.List;

import com.vates.facpro.persistence.domain.RoleView;
import com.vates.facpro.persistence.domain.User;
import com.vates.facpro.persistence.domain.UserView;
import com.vates.facpro.persistence.paging.Page;

/**
 * @author Gaston Napoli
 * 
 */
public interface UserService extends BaseService<User, Long> {

	/**
	 * Finds a user by its login and password.
	 * 
	 * @param login
	 * @param password
	 * @return the user found, null otherwise.
	 */

	User find(String login);
	
	User findById(Long id);

	User find(String login, String password);
	
    UserView findView(String login);

	UserView findView(String login, String password);
	
	public String getUserNameAndLastName(Long id);

	boolean existUser(String mail, Long id);

	boolean existUserId(Long id);
	
	boolean existUserIdAndRolesId(Long id, Long rolId);

	boolean existWF(Long id);

	Page<UserView> findPaginatedFilteredList(String name, String lastName,
			int active, String job, int pageIndex, int rowsPerPage);

	void saveUser(User user) throws Exception;

	void deleteUser(User user);

	void updateUser(User user);
	
	List<UserView> getAuthorizersList();
	
	UserView getUserView(Long id);
	
	List<RoleView> getUserRolesMenu(Long id);
	
	List<RoleView> findUserRoles(Long id);
	
	

}
