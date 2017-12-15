package com.vates.facpro.persistence.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.persistence.OptimisticLockException;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.Role;
import com.vates.facpro.persistence.domain.User;
import com.vates.facpro.persistence.mail.MailUtil;
import com.vates.facpro.persistence.mail.TemplateLoader;
import com.vates.facpro.persistence.paging.Page;
import com.vates.facpro.persistence.paging.PageConverter;
import com.vates.facpro.persistence.repository.NivelRepository;
import com.vates.facpro.persistence.repository.RoleRepository;
import com.vates.facpro.persistence.repository.UserRepository;
import com.vates.facpro.persistence.service.UserService;

/**
 * @author
 * 
 */
@Repository("userService")
@Transactional(propagation = Propagation.NESTED)
public class UserServiceImpl extends AbstractBaseService<User, Long> implements
		UserService {

	@Inject
	private UserRepository repository;

	@Inject
	private PageConverter<User> pageConverter;

	@Inject
	private MailUtil mailUtil;

	@Inject
	private TemplateLoader templateLoader;

	@Inject
	private NivelRepository nivelRepository;

	@Inject
	private RoleRepository roleRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vates.eng.ha.persistence.service.impl.AbstractBaseService#getRepository
	 * ()
	 */
	@Override
	protected JpaRepository<User, Long> getRepository() {
		return repository;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vates.eng.ha.persistence.service.impl.AbstractBaseService#
	 * getPageConverter()
	 */
	@Override
	protected PageConverter<User> getPageConverter() {
		return pageConverter;
	}

	@Override
	public User find(String login) {
		return repository.findByMailIgnoringCase(login + "@vates.com");
	}

	@Override
	public User find(String login, String password) {
		return repository.findByMailIgnoringCaseAndPassword(login
				+ "@vates.com", password);
	}

	@Override
	public boolean existUser(User user) {
		return repository.findByMailIgnoringCase(user.getMail()) != null ? true
				: false;
	}

	@Override
	public boolean existUserId(Long id) {
		return repository.findOne(id) != null ? true : false;
	}
	
	public boolean existUserIdAndRolesId(Long id, Long rolId) {
		return repository.findByIdAndRolesId(id, rolId) != null ? true : false;
	}

	@Override
	public boolean existUserUpdate(User user) {
		return repository.findByMailIgnoringCaseAndIdNot(user.getMail(),
				user.getId()) != null ? true : false;
	}

	@Override
	public boolean existWF(Long id) {
		boolean result = true;
		if(nivelRepository.findByUsuarioId(id).isEmpty()){
			result = false;			
		}
		return result;
	}
	
	@Override
	public String getUserNameAndLastName(Long id) {
		return repository.findNameAndLastNAmeById(id);
	}
	
	

	@Override
	@Transactional(readOnly = true)
	public Page<User> findPaginatedFilteredList(String name, String lastName,
			int active, String job, int pageIndex, int rowsPerPage) {
		return getPageConverter()
				.convertFrom(
						active < 0 ? repository
								.findByNameIsLikeIgnoringCaseAndLastNameIsLikeIgnoringCaseAndJobIsLikeAndIdNot(
										prepareString(name),
										prepareString(lastName),
										prepareString(job),
										1L,
										constructPageSpecification(pageIndex,
												rowsPerPage))
								: repository
										.findByNameIsLikeIgnoringCaseAndLastNameIsLikeIgnoringCaseAndActiveAndJobIsLikeAndIdNot(
												prepareString(name),
												prepareString(lastName),
												active,
												prepareString(job),
												1L,
												constructPageSpecification(
														pageIndex, rowsPerPage)));
	}

	@Override
	public void saveUser(User user) throws MessagingException {
		generateNewPassUser(user);
		user.setTipo(User.DOMAIN_VATESSA);
		user.setJob("".equals(user.getJob()) ? " " : user.getJob());
		user.setRoles(new HashSet<Role>());
		user.getRoles().add(roleRepository.findOne(User.USUARIO_FINAL));
		repository.save(user);
		repository.flush();
		Map<String, String> map = new HashMap<String, String>();
		map.put("FIRMA", "Se creó el usuario " + subString(user.getMail())
				+ " en Sistemas Internos");
		map.put("URL", templateLoader.getUrlBase());
		mailUtil.sendMessage(user.getMail(), "Alta de usuario Sistemas Internos",
				templateLoader.getTemplate("newUser", map), false);

	}

	@Override
	public void updateUser(User user) {
		User oldUser = repository.findOne(user.getId());
		if (user.getVersion() < oldUser.getVersion()) {
			throw new OptimisticLockException();
		}
		updateUser(oldUser, user);
		repository.save(oldUser);
	}

	@Override
	public void forceUpdateUser(User user) {
		User oldUser = repository.findOne(user.getId());
		updateUser(oldUser, user);
		repository.save(oldUser);
	}

	@Override
	public void deleteUser(User user) {
		repository.delete(user);
	}

	@Override
	public void forceDeleteUser(User user) {
		User deleteUser = repository.findOne(user.getId());
		repository.delete(deleteUser);
	}

	private String subString(String input) {
		int finish = input.indexOf("@");
		input = input.substring(0, finish);
		return input;
	}

	private String prepareString(String input) {
		StringBuffer result = new StringBuffer();
		result.append("%");
		result.append(input);
		result.append("%");
		return result.toString();
	}

	private Pageable constructPageSpecification(int pageIndex, int rowsPerPage) {
		Pageable pageSpecification = new PageRequest(pageIndex, rowsPerPage,
				sortByLastNameAsc());
		return pageSpecification;
	}

	private Sort sortByLastNameAsc() {
		return new Sort(Sort.Direction.ASC, "lastName");
	}

	private User generateNewPassUser(User user) {
		user.setPassword(PassworGenerator.generatePassword());
		return user;
	}

	private void updateUser(User oldUser, User newUser) {
		oldUser.setActive(newUser.getActive());
		oldUser.setJob("".equals(newUser.getJob()) ? " " : newUser.getJob());
		oldUser.setLastName(newUser.getLastName());
		oldUser.setMail(newUser.getMail());
		oldUser.setName(newUser.getName());
	}

	@Override
	public List<User> getAuthorizersList() {
		return repository.findByRolesIdAndActive(Role.USER_FINAL, 1);
	}

}
