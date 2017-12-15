package com.vates.facpro.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.Proveedor;

@Repository
@Transactional(readOnly = true)
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

	Proveedor findById(long id);
	
	Proveedor findByIdAndDeleted(long id, Boolean deleted);


}
