package com.vates.facpro.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.ArchivoFactura;

@Repository
@Transactional(readOnly = true)
public interface ArchivoFacturaRepository extends
		JpaRepository<ArchivoFactura, Long> {
	
	List<ArchivoFactura> findByFacturaId(Long facturaId);
	
	List<ArchivoFactura> findByFacturaIdAndArchivoHeaderDeleted(Long facturaId, Boolean deleted);
	
	ArchivoFactura findByArchivoHeaderIdAndFacturaNivelIsNotNull(Long archivoHeaderId);
	
	List<ArchivoFactura> findByFacturaIdAndFacturaNivelIsNotNull(Long facturaId);
	
	@Query("SELECT ah.id FROM ArchivoFactura as af INNER JOIN af.archivoHeader as ah where af.factura.id=:facturaId and ah.fileType=:type and ah.deleted=:deleted")
	Long findArchivoHeaderIdByFacturaIdAndArchivoHeaderFileTypeAndArchivoHeaderDeleted(
			@Param("facturaId") Long facturaId, @Param("type") Integer type,
			@Param("deleted") Boolean deleted);

}
