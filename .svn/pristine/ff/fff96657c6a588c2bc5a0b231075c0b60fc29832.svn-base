package com.vates.facpro.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.ArchivoFactura;

@Repository
@Transactional(readOnly = true)
public interface ArchivoFacturaRepository extends
		JpaRepository<ArchivoFactura, Long> {
	
	List<ArchivoFactura> findByFacturaId(Long facturaId);
	
	ArchivoFactura findByArchivoHeaderIdAndFacturaNivelIsNotNull(Long archivoHeaderId);

}
