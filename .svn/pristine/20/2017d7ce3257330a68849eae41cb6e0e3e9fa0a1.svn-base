package com.vates.facpro.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.People;

@Repository
@Transactional(readOnly = true)
public interface PeopleRepository extends JpaRepository<People, Long> {

	
	People findById(Long id);
	
	List<People> findByPrefaId(Long prefaId);


}