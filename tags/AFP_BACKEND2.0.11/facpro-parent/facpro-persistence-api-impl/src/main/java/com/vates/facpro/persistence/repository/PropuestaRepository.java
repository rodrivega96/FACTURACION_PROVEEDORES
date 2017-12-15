package com.vates.facpro.persistence.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.Propuesta;

@Repository
@Transactional(readOnly = true)
public interface PropuestaRepository extends JpaRepository<Propuesta, Long> {

	
	Propuesta findById(Long id);
	
	List<Propuesta> findByComercialIsLikeIgnoreCaseAndDescripcionIsLikeIgnoreCase(String comercial, String Descripcion);
	
	List<Propuesta> findByComercialLikeIgnoreCaseAndDescripcionLikeIgnoreCaseAndVigenteDesdeGreaterThanEqualAndVigenteHastaLessThanEqual(String comercial, String Descripcion, Date desde, Date hasta);
}
