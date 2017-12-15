package com.vates.facpro.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.MedioPago;

@Repository
@Transactional(readOnly = true)
public interface MedioPagoRepository extends JpaRepository<MedioPago, Long> {

	MedioPago findById(Long id);
	
	List<MedioPago> findByIdIn(List<Long> ids);

}
