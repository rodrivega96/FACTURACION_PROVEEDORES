package com.vates.facpro.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.HistorialAutorizacion;

@Repository
@Transactional(readOnly = true)
public interface HistorialAutorizacionRepository extends
		JpaRepository<HistorialAutorizacion, Long> {

	List<HistorialAutorizacion> findByFacturaIdOrderByUpdatedAtAsc(
			Long facturaId);

	List<HistorialAutorizacion> findByFacturaIdAndNivelEliminadoOrderByUpdatedAtAsc(
            Long facturaId,Boolean eliminada);
	
	boolean findByEstadoOrEstado(Long observada, Long observadaSup);
}
