package com.vates.facpro.persistence.service.impl;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.Role;
import com.vates.facpro.persistence.domain.User;
import com.vates.facpro.persistence.paging.PageConverter;
import com.vates.facpro.persistence.repository.RoleRepository;
import com.vates.facpro.persistence.repository.UserRepository;
import com.vates.facpro.persistence.service.RoleService;

/**
 * @author
 * 
 */
@Repository("roleService")
@Transactional(propagation = Propagation.NESTED)
public class RoleServiceImpl extends AbstractBaseService<Role, Long> implements
		RoleService {

	@Inject
	private RoleRepository repository;

	@Inject
	private UserRepository userRepository;

	@Inject
	private PageConverter<Role> pageConverter;

	@Override
	protected JpaRepository<Role, Long> getRepository() {
		return repository;
	}

	@Override
	protected PageConverter<Role> getPageConverter() {
		return pageConverter;
	}

	@Override
	public void saveRoles(Long userId, Set<Role> roles) {
		User us = userRepository.findOne(userId);
		us.setRoles(roles);
		userRepository.save(us);
	}

	@Override
	public List<Role> findAllRole() {
		return repository.findByIdNot(1L);
	}

}
