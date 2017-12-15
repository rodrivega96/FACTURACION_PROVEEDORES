package com.vates.facpro.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.NivelPublicado;

@Repository
@Transactional(readOnly = false)
public interface NivelPublicadoRepository extends JpaRepository<NivelPublicado, Long> {

	NivelPublicado findNivelPublicadoByFacturaIdAndLast(Long id, boolean last);

	List<NivelPublicado> findFacturaByLast(boolean last);	

}
