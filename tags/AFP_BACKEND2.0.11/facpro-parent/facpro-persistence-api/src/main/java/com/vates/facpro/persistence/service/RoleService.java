package com.vates.facpro.persistence.service;

import java.util.List;
import java.util.Set;

import com.vates.facpro.persistence.domain.Role;

/**
 * @author Gaston Napoli
 * 
 */
public interface RoleService extends BaseService<Role, Long> {

	void saveRoles(Long userId, Set<Role> roles);

	List<Object[]> findAllRole();
	
	Role findById(Long id);

}
