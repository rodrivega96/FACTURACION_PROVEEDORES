package com.vates.facpro.persistence.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.FacturaAdm;

@Repository
@Transactional(readOnly = true)
public interface FacturaAdmRepository extends JpaRepository<FacturaAdm, Long>{



    @Query(value="FROM FacturaAdm facAdm "
    		+ " WHERE facAdm.id in (?1)", nativeQuery=false)
	Page<FacturaAdm> findByIdIn(List<Long> numero, Pageable pageable);
    
    @Query(value="FROM FacturaAdm facAdm "
    		, nativeQuery=false)
	Page<FacturaAdm> findByNone(Pageable pageable);
}
