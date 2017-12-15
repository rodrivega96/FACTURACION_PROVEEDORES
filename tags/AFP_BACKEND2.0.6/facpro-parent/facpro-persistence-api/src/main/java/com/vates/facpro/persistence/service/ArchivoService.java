package com.vates.facpro.persistence.service;

import com.vates.facpro.persistence.domain.Archivo;
import com.vates.facpro.persistence.domain.ArchivoHeader;

/**
 * @author
 * 
 */
public interface ArchivoService extends BaseService<Archivo, Long> {

	Archivo findByHeaderId(Long idHeader);

	void saveFile(byte[] byteArray, ArchivoHeader archivoHeader);

	void deleteFile(Long idArchivo);

}
