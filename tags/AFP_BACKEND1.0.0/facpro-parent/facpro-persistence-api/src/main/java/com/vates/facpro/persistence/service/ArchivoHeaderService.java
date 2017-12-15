package com.vates.facpro.persistence.service;

import com.vates.facpro.persistence.domain.ArchivoHeader;

/**
 * @author
 * 
 */
public interface ArchivoHeaderService extends BaseService<ArchivoHeader, Long> {

	boolean isUnique(String fileName, Long idFactura);

	Long getSizeMax();
	
	Boolean canDelete(Long idHeader);

}
