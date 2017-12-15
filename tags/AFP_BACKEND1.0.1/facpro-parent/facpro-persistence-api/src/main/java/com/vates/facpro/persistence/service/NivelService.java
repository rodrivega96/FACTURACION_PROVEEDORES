package com.vates.facpro.persistence.service;

import java.util.List;

import com.vates.facpro.persistence.domain.Nivel;

/**
 * @author cparodi
 * 
 */
public interface NivelService extends BaseService<Nivel, Long> {

	Nivel find(long id);
	
	List<Nivel> findNivelByFacturaId(long id);
	
	List<Nivel> findNivelByFacturaIdAndEliminado(long id, boolean eliminado);

	Nivel saveNivel(Nivel nivel);
	
	void saveNiveles(List<Nivel> niveles) throws Exception;
	
	void deleteByFacturaId(Long id);
	
	void informarPendienteAutorizar(Nivel nivel) throws Exception;
	
	void flush();
	
}
