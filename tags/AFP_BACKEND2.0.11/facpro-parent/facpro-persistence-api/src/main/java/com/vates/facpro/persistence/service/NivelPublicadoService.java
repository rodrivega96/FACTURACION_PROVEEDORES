package com.vates.facpro.persistence.service;

import java.util.List;

import com.vates.facpro.persistence.domain.NivelPublicado;

/**
 * @author
 * 
 */
public interface NivelPublicadoService extends BaseService<NivelPublicado, Long> {

	NivelPublicado find(Long id);
	
	NivelPublicado findNivelPublicadoByFacturaIdAndLast(Long id);

	NivelPublicado saveNivelPublicado(NivelPublicado nivelPublicado);
	
//	void informarPendienteAutorizar(Nivel nivel) throws Exception;

	List<NivelPublicado> findFacturaByLast(boolean last);
	
	List<NivelPublicado> findByFacturaId(Long id);
	
	Long countNivelesByFacturaIdAndLastAndNivelEstadoNotIn(Long id, List<Integer> estados);
		
}
