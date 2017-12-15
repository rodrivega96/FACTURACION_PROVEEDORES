package com.vates.facpro.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.FacturaCliente;

@Repository
@Transactional(readOnly = true)
public interface FacturaClienteRepository extends
		JpaRepository<FacturaCliente, Long> {

	@Query("SELECT fc FROM FacturaCliente as fc, FacturasClientePorOC as fcoc where fc.id = fcoc.idFactura and fcoc.idOrden = (:ordenId)")
	List<FacturaCliente> findFacturaByOrdenId(@Param("ordenId") Long ordenId);

	@Query("SELECT fc FROM FacturaCliente as fc where fc.clienteId = (:clienteId) and fc.id not in (SELECT fcoc.idFactura FROM FacturasClientePorOC as fcoc, FacturaCliente as fc1 where fcoc.idFactura = fc1.id and fc1.clienteId = (:clienteId) and fcoc.idOrden != (:idOrden))")
	List<FacturaCliente> findByClienteIdAndNotFacturaPorOC(
			@Param("clienteId") Long clienteId, @Param("idOrden") Long idOrden);

}
