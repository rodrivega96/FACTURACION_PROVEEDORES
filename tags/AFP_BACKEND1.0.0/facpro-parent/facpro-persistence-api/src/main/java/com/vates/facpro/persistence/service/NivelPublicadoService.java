package com.vates.facpro.persistence.service;

import java.util.List;

import com.vates.facpro.persistence.domain.Nivel;
import com.vates.facpro.persistence.domain.NivelPublicado;

/**
 * @author
 * 
 */
public interface NivelPublicadoService extends BaseService<NivelPublicado, Long> {

	NivelPublicado find(long id);
	
	NivelPublicado findNivelPublicadoByFacturaIdAndLast(long id);

	NivelPublicado saveNivelPublicado(NivelPublicado nivelPublicado);
	
	void informarPendienteAutorizar(Nivel nivel) throws Exception;

	List<NivelPublicado> findFacturaByLast(boolean last);
	
	//void flush();
	
}
