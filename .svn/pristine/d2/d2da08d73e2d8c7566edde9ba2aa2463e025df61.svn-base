package com.vates.facpro.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.Factura;

@Repository
@Transactional(readOnly = true)
public interface FacturaRepository extends JpaRepository<Factura, Long> {

	
	Factura findByFacturaAdmId(Long idFacturaAdm);

	Factura findById(long id);
}
