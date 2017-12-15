package com.vates.facpro.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.Factura;

@Repository
@Transactional(readOnly = true)
public interface FacturaRepository extends JpaRepository<Factura, Long> {

	
	Factura findByFacturaAdm(Long idFacturaAdm);

	Factura findById(long id);
	
	List<Factura> findByEstadoAndFacturaAdmIsNotNull(long id);
	
	List<Factura> findByEstadoAndTipoFacturaAndFacturaAdmIsNotNull(long id, long tipo);
	
	List<Factura> findByEstadoAndTipoFacturaNotAndFacturaAdmIsNotNull(long id, long tipo);
	
	@Query("SELECT COUNT(f) FROM Factura f WHERE f.id = :id AND f.estado = :estado")
	Long countByFacturaIdAndEstado(@Param("id") Long facturaId, @Param("estado") Long estado);
}
