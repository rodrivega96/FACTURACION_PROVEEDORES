package com.vates.facpro.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.Orden;

/**
 * @author Lucas Scarlatta
 * 
 */

@Repository
@Transactional(readOnly = true)
public interface OrdenRepository extends JpaRepository<Orden, Long> {

	Orden findById(Long id);

	@Query("SELECT o.numeroInternaPrefix, o.numeroInternaSufix FROM Orden as o WHERE o.tipo=:tipo and o.createdAt = (SELECT max(o1.createdAt) FROM Orden as o1 WHERE o1.tipo=:tipo)")
	Object findNumeroByTipoAndMaxFechaEmision(@Param("tipo") Long tipo);

	Orden findByNumeroAndTipoAndActiva(String numero, Long tipo, Long activa);
	
	List<Orden> findByEstadoIn(List<Long> estados);
	
	//List<Orden> findByEstadoInAndSaldoLessThanEqual(List<Long> estados, Double saldo);

}
