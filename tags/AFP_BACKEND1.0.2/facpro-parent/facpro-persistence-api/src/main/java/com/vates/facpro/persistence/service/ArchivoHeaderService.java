package com.vates.facpro.persistence.service;

import com.vates.facpro.persistence.domain.ArchivoHeader;

/**
 * @author
 * 
 */
public interface ArchivoHeaderService extends BaseService<ArchivoHeader, Long> {

	boolean isUnique(String fileName, Long idFactura, Integer fileType, Boolean force);

	Long getSizeMax();
	
	Boolean canDelete(Long idHeader);

}
