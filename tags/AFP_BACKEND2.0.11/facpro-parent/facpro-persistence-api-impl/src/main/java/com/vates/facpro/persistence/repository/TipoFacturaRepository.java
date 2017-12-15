package com.vates.facpro.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.TipoFactura;

@Repository
@Transactional(readOnly = true)
public interface TipoFacturaRepository extends JpaRepository<TipoFactura, Long> {
	
	@Query("SELECT tf.nombre FROM TipoFactura tf where tf.id=:id")
	String findNameById(@Param("id") Long id);

}
