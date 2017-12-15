package com.vates.facpro.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.Role;

/**
 * @author
 * 
 */
@Repository
@Transactional(readOnly = true)
public interface RoleRepository extends JpaRepository<Role, Long> {

	Role findById(Long id);

	@Query("SELECT rol.id, rol.name, rol.description"
			+ " FROM Role as rol WHERE rol.id NOT IN (:listId)")
	List<Object[]> findByNotIdList(@Param("listId") List<Long> listId);

}
