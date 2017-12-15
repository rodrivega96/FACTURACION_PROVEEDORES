package com.vates.facpro.persistence.service;

import java.util.List;

import com.vates.facpro.persistence.domain.ArchivoOrden;
import com.vates.facpro.persistence.domain.Orden;

/**
 * @author
 * 
 */
public interface ArchivoOrdenService extends BaseService<ArchivoOrden, Long> {

	void saveAll(List<ArchivoOrden> archivosOrden);

	List<ArchivoOrden> findByOrdenIdAndDelete(Long ordenId, Boolean delete);
	
	void createArchivosOrden(Long archivoHeaderId, Orden orden);
	
	void deletebyOcId(Long id);

	ArchivoOrden updateFile(String fileName, String type, Long idOrden,
			Integer fileType, Long userId);

	Long findArchivoHeaderIdByOrdenIdAndArchivoHeaderFileTypeAndArchivoHeaderDeleted(
			Long ordenId, Integer type, Boolean deleted);

}
