package com.vates.facpro.persistence.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.User;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

	User findByMailIgnoringCase(String mail);

	User findByMailIgnoringCaseAndIdNot(String mail, Long id);

	User findByMailIgnoringCaseAndPassword(String mail, String password);

	User findByMail(String mail);
	
	
	@Query("SELECT usr.mail FROM User as usr where usr.id in (:userIds)")
	List<String> findMailByIdIn(@Param("userIds") List<Long> userIds);
	
	@Query("SELECT usr.mail FROM User as usr where usr.id=:userId")
	String findMailById(@Param("userId") Long userId);

	Page<User> findByNameIsLikeIgnoringCaseAndLastNameIsLikeIgnoringCaseAndActiveAndJobIsLikeAndIdNot(
			String name, String lastName, int active, String job, Long id,
			Pageable pageable);

	Page<User> findByNameIsLikeIgnoringCaseAndLastNameIsLikeIgnoringCaseAndJobIsLikeAndIdNot(
			String name, String lastName, String job, Long id, Pageable pageable);

	List<User> findByRolesIdAndActive(Long roleId, int active);
	
	User findByIdAndRolesId(Long id, Long rolId);
}
