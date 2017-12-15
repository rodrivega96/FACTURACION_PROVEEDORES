package com.vates.facpro.persistence.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.CuentasExcluidas;

@Repository
@Transactional(readOnly = true)
public interface CuentasExcluidasRepository extends JpaRepository<CuentasExcluidas, Long> {

	@Query("SELECT ce.jerarquia FROM CuentasExcluidas ce")
	Set<String> findJerarquiasExcluidas();
}
