package com.vates.facpro.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.Archivo;

/**
 * @author
 * 
 */
@Repository
@Transactional(readOnly = true)
public interface ArchivoRepository extends JpaRepository<Archivo, Long> {

	Archivo findByArchivoHeaderId(Long id);

	@Modifying
	@Query("DELETE FROM Archivo as arch WHERE arch.archivoHeader.id=:idHeader")
	void deleteArchivoByIdHeader(@Param("idHeader") Long idHeader);
	
	@Modifying
	@Query("DELETE FROM Archivo as arch WHERE arch.archivoHeader.id in (select id FROM ArchivoHeader WHERE idTmp like :idHeader)")
	void deleteArchivoByIdHeaderTemp(@Param("idHeader") String idHeader);

}
