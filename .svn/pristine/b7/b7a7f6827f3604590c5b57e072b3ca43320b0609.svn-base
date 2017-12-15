package com.vates.facpro.persistence.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.User;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

	// public User findByLoginIgnoringCaseAndPasswordAndActive(String login,
	// String password, boolean active);

	User findByMailIgnoringCase(String mail);

	User findByMailIgnoringCaseAndIdNot(String mail, Long id);

	User findByMailIgnoringCaseAndPassword(String mail, String password);

	User findByMail(String mail);

	Page<User> findByNameIsLikeIgnoringCaseAndLastNameIsLikeIgnoringCaseAndActiveAndJobIsLikeAndIdNot(
			String name, String lastName, int active, String job, Long id,
			Pageable pageable);

	Page<User> findByNameIsLikeIgnoringCaseAndLastNameIsLikeIgnoringCaseAndJobIsLikeAndIdNot(
			String name, String lastName, String job, Long id, Pageable pageable);

	List<User> findByRolesIdAndActive(Long roleId, int active);
}
