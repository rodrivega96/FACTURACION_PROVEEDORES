package com.vates.facpro.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.Nivel;

@Repository
@Transactional(readOnly = false)
public interface NivelRepository extends JpaRepository<Nivel, Long> {

	List<Nivel> findNivelByFacturaId(Long id);

	List<Nivel> findNivelByFacturaIdAndEliminado(long id, boolean eliminado);
	
	List<Nivel> findByUsuarioId(Long id);
	
	Long countByFacturaId(Long id);

}
