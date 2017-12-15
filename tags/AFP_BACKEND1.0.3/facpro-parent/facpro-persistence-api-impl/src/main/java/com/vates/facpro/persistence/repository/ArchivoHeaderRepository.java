package com.vates.facpro.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.ArchivoHeader;

/**
 * @author
 * 
 */
@Repository
@Transactional(readOnly = true)
public interface ArchivoHeaderRepository extends
		JpaRepository<ArchivoHeader, Long> {
	
	ArchivoHeader findByNameIgnoringCase(String name);

}
