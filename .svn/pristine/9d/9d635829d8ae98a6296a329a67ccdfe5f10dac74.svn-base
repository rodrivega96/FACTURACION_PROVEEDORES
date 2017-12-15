package com.vates.facpro.persistence.service;

import java.util.List;

import com.vates.facpro.persistence.domain.ArchivoHeader;

/**
 * @author
 * 
 */
public interface ArchivoHeaderService extends BaseService<ArchivoHeader, Long> {

	boolean isUnique(String fileName, Long idFactura, Integer fileType, Boolean force);

	Long getSizeMax();

	Boolean canDelete(Long idHeader);

	ArchivoHeader saveFile(String fileName, String type, String idTmp,
			Integer fileType, Long idUsuario);

	ArchivoHeader updateFile(String fileName, String type, Long idOrden,
			Integer fileType, Long idUsuario);

	List<ArchivoHeader> findByIdTmp(String idTmp);
	
	ArchivoHeader findOneByIdTmp(String idTmp);
	
	void deleteByHeader(ArchivoHeader archivoHeader, Boolean force);

	boolean isUniqueFileOrden(String fileName, Integer fileType, Long idOrden);
	
	void deleteByTempId(String tempId);

	boolean isUniqueFileTmp(String fileName, Integer fileType, String idTmp);

}
