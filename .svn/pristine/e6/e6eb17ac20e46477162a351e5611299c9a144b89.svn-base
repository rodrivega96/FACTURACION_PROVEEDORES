package com.vates.facpro.persistence.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
	
	ArchivoHeader findById(Long id);
	
	List<ArchivoHeader> findByIdTmp(String idTmp);
	
	ArchivoHeader findOneByIdTmp(String idTmp);

	List<ArchivoHeader> findByIdTmpIsNotNullAndCreatedAtLessThan(Date dateDelete);
	
	@Modifying
	@Query("DELETE FROM ArchivoHeader as arch WHERE idTmp=:idHeader")
	void deleteHeaderByIdHeaderTemp(@Param("idHeader") String idHeader);
	
	
	@Query("select count(id) FROM ArchivoHeader as arch WHERE idTmp=:idHeader")
	Integer countHeaderByIdHeaderTemp(@Param("idHeader") String idHeader);

}
