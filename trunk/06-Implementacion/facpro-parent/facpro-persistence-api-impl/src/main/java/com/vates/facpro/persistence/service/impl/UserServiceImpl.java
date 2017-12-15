package com.vates.facpro.persistence.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.mail.MessagingException;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.Role;
import com.vates.facpro.persistence.domain.RoleView;
import com.vates.facpro.persistence.domain.User;
import com.vates.facpro.persistence.domain.UserView;
import com.vates.facpro.persistence.mail.MailUtil;
import com.vates.facpro.persistence.mail.TemplateLoader;
import com.vates.facpro.persistence.paging.Page;
import com.vates.facpro.persistence.paging.PageConverter;
import com.vates.facpro.persistence.repository.NivelRepository;
import com.vates.facpro.persistence.repository.RoleRepository;
import com.vates.facpro.persistence.repository.UserRepository;
import com.vates.facpro.persistence.service.UserService;
import com.vates.facpro.persistence.util.ParseoService;

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
	private PageConverter<UserView> pageConverterUserView;

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
	
	protected PageConverter<UserView> getPageConverterUserView() {
		return pageConverterUserView;
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
	public UserView findView(String login) {
		return UserView.loadCompleteUserView((Object[])repository.findUserViewByMailIgnoringCase(login + "@vates.com"));
	}

	@Override
	public UserView findView(String login, String password) {
		return UserView.loadCompleteUserView((Object[])repository.findUserViewByMailIgnoringCaseAndPassword(login
				+ "@vates.com", password));
	}

	@Override
	public boolean existUser(String mail, Long id) {
		return repository.countByMailIgnoringCaseAndIdNot(mail,
				id) > 0 ? true : false;
	}

	@Override
	public boolean existUserId(Long id) {
		return repository.countById(id) > 0 ? true : false;
	}
	
	@Override
	public boolean existUserIdAndRolesId(Long id, Long rolId) {
		return repository.countByIdAndRolesId(id, rolId) > 0 ? true : false;
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
		return repository.findNameAndLastNameById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<UserView> findPaginatedFilteredList(String name,
			String lastName, int active, String job, int pageIndex,
			int rowsPerPage) {
		List<Long> idList = new ArrayList<Long>();
		idList.add(User.ROOT_USER);
		List<Integer> activeList = new ArrayList<Integer>();
		if (active < 0) {
			activeList.add(User.INACTIVE);
			activeList.add(User.ACTIVE);
		} else {
			activeList.add(active);
		}
		List<Object[]> userView = repository.findPaginatedList(activeList,
				ParseoService.prepareString(name),
				ParseoService.prepareString(lastName),
				ParseoService.prepareString(job), idList,
				constructPageSpecification(pageIndex, rowsPerPage));
		Long size = repository.countPaginatedList(activeList,
				ParseoService.prepareString(name),
				ParseoService.prepareString(lastName),
				ParseoService.prepareString(job), idList);
		List<UserView> listUser = UserView.loadUserViewList(userView);
		return getPageConverterUserView().convertFrom(
				new PageImpl<UserView>(listUser, constructPageSpecification(
						pageIndex, rowsPerPage), size));
	}

	@Override
	public void saveUser(User user) throws MessagingException {
		generateNewPassUser(user);
		user.setTipo(User.DOMAIN_VATESSA);
		user.setRoles(new HashSet<Role>());
		user.getRoles().add(roleRepository.findById(User.USUARIO_FINAL));
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
		repository.save(user);
	}

	@Override
	public void deleteUser(User user) {
		repository.delete(user);
	}

	private String subString(String input) {
		int finish = input.indexOf("@");
		input = input.substring(0, finish);
		return input;
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

	@Override
	public List<UserView> getAuthorizersList() {
		return UserView.loadUserViewList(repository.findBasicByRolesIdAndActive(Role.USER_FINAL, 1));
	}
	
	@Override
	public List<UserView> getAuthorizersList(Boolean admin, Long userId) {
		if(admin){
			return UserView.loadUserViewList(repository.findBasicByRolesIdAndActive(Role.USER_FINAL, 1));
		}
		List<UserView> list = new ArrayList<UserView>();
		list.add(UserView.loadUserView((Object[])repository.findSingleBasicById(userId)));
		return list;
	}

	@Override
	public UserView getUserView(Long id) {
		return UserView.loadUserView((Object[])repository.findSingleBasicById(id));
	}
	
	

	@Override
	public User findById(Long id) {
		return repository.findById(id);
	}

	@Override
	public List<RoleView> getUserRolesMenu(Long id) {
		return RoleView.loadCompleteRoleViewList(repository.findRolesMenuUser(id));
	}

	@Override
	public List<RoleView> findUserRoles(Long id) {
		return RoleView.loadRoleViewList(repository.findUserRoles(id));
	}

}
