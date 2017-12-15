package com.vates.facpro.persistence.service;

import java.util.List;

import com.vates.facpro.persistence.domain.ArchivoFactura;

/**
 * @author
 * 
 */
public interface ArchivoFacturaService extends
		BaseService<ArchivoFactura, Long> {

	ArchivoFactura saveFile(String fileName, String type, Long idFactura,
			Integer fileType, Long idUsuario);

	List<ArchivoFactura> findByFacturaId(Long facturaId);

	Long findArchivoHeaderIdByFacturaIdAndArchivoHeaderFileTypeAndArchivoHeaderDeleted(
			Long facturaId, Integer type, Boolean deleted);
	
	boolean canDelete(Long facturaId);

}
