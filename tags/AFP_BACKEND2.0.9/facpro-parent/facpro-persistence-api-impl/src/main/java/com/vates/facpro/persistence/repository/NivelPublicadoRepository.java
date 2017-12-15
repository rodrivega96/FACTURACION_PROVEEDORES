package com.vates.facpro.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.NivelPublicado;

@Repository
@Transactional(readOnly = false)
public interface NivelPublicadoRepository extends JpaRepository<NivelPublicado, Long> {

	NivelPublicado findNivelPublicadoByFacturaIdAndLast(Long id, boolean last);
	
	@Query("SELECT COUNT (n) FROM NivelPublicado np INNER JOIN np.niveles n "
			+ "WHERE np.factura.id = :id AND np.last= :last AND n.estado not in (:estados)")
	Long countNivelesByFacturaIdAndLastAndNivelEstadoNotIn(@Param("id") Long id, 
			@Param("last") boolean last, @Param("estados") List<Integer> estados);

	List<NivelPublicado> findFacturaByLast(boolean last);	
	
	List<NivelPublicado> findByFacturaId(Long id);	

}
