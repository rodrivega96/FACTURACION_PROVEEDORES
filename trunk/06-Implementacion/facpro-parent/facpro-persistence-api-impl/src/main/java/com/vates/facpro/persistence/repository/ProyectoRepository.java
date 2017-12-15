package com.vates.facpro.persistence.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.Proyecto;

@Repository
@Transactional(readOnly = true)
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

	
	Proyecto findById(Long id);
	
	List<Proyecto> findByIdIn(Set<Long> id);

	List<Proyecto> findByNombreIsLikeIgnoreCaseAndActivoAndPmgIsLikeIgnoreCaseOrNombreIsLikeIgnoreCaseAndActivoAndPmIsLikeIgnoreCase(
			String nombre1, Integer activo1, String pmg, String nombre2,
			Integer activo2, String pm);

	List<Proyecto> findByNombreIsLikeIgnoreCaseAndPmgIsLikeIgnoreCaseOrNombreIsLikeIgnoreCaseAndPmIsLikeIgnoreCase(
			String nombre1, String pmg, String nombre2, String pm);

}