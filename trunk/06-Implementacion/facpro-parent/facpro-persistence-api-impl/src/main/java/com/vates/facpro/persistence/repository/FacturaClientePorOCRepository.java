package com.vates.facpro.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.FacturasClientePorOC;

@Repository
@Transactional(readOnly = true)
public interface FacturaClientePorOCRepository extends
		JpaRepository<FacturasClientePorOC, Long> {

	@Query("SELECT fcoc.idOrden, sum(fc.total) FROM FacturasClientePorOC as fcoc, FacturaCliente as fc where fcoc.idFactura = fc.id and fcoc.idOrden in (:ordenIds) group by fcoc.idOrden")
	List<Object[]> findFacturaTotalAndOrdenId(
			@Param("ordenIds") List<Long> ordenIds);

	@Query("SELECT fcoc.idFactura FROM FacturasClientePorOC as fcoc where fcoc.idOrden = (:ordenId)")	
	List<Long> findFacturaIdByOrdenId(@Param("ordenId") Long ordenId);

	List<FacturasClientePorOC> findByIdOrden(Long ordenId);
}
