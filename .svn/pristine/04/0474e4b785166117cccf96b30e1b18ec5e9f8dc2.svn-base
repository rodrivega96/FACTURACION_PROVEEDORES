package com.vates.facpro.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.TipoTelefono;

@Repository
@Transactional(readOnly = true)
public interface TipoTelefonoRepository extends JpaRepository<TipoTelefono, Long> {
	TipoTelefono findById(Long id);
}
