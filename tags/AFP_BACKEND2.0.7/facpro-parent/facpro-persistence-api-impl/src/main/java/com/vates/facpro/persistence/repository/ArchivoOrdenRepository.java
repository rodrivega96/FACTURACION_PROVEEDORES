package com.vates.facpro.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.ArchivoOrden;

@Repository
@Transactional(readOnly = true)
public interface ArchivoOrdenRepository extends
		JpaRepository<ArchivoOrden, Long> {

	List<ArchivoOrden> findByOrdenIdAndArchivoHeaderDeleted(Long ordenId, Boolean delete);
	
	List<ArchivoOrden> findByOrdenId(Long ordenId);

	@Query("SELECT ah.id FROM ArchivoOrden as ao INNER JOIN ao.archivoHeader as ah where ao.orden.id=:ordenId and ah.fileType=:type and ah.deleted=:deleted")
	Long findArchivoHeaderIdByFacturaIdAndArchivoHeaderFileTypeAndArchivoHeaderDeleted(
			@Param("ordenId") Long ordenId, @Param("type") Integer type,
			@Param("deleted") Boolean deleted);
	
	@Query("SELECT count(ao) FROM ArchivoOrden ao INNER JOIN ao.archivoHeader ah where ao.orden.id = :ordenId and ah.fileType = :fileType and ah.deleted = :deleted")
	Long countByOrdenIdAndDeletedAndFileType(@Param("ordenId") Long ordenId, @Param("deleted") Boolean deleted, @Param("fileType") Integer fileType);

}