package com.vates.facpro.persistence.service;

import java.util.List;

import com.vates.facpro.persistence.domain.User;
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

	User find(String login, String password);

	User resetPass(User user);

	boolean existUser(User user);

	boolean existUserUpdate(User user);

	boolean existUserId(Long id);
	
	boolean existUserIdAndRolesId(Long id, Long rolId);

	boolean existWF(Long id);

	Page<User> findPaginatedFilteredList(String name, String lastName,
			int active, String job, int pageIndex, int rowsPerPage);

	void saveUser(User user) throws Exception;

	void deleteUser(User user);

	void forceDeleteUser(User user);

	void updateUser(User user);

	void forceUpdateUser(User user);
	
	List<User> getAuthorizersList();

}
