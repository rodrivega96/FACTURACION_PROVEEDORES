package com.vates.facpro.persistence.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.ArchivoFactura;
import com.vates.facpro.persistence.domain.ArchivoHeader;
import com.vates.facpro.persistence.domain.ArchivoOrden;
import com.vates.facpro.persistence.paging.PageConverter;
import com.vates.facpro.persistence.repository.ArchivoFacturaRepository;
import com.vates.facpro.persistence.repository.ArchivoHeaderRepository;
import com.vates.facpro.persistence.repository.ArchivoOrdenRepository;
import com.vates.facpro.persistence.repository.ArchivoRepository;
import com.vates.facpro.persistence.service.ArchivoHeaderService;

/**
 * @author
 * 
 */
@Repository("ArchivoHeaderService")
@Transactional(propagation = Propagation.NESTED)
public class ArchivoHeaderServiceImpl extends
		AbstractBaseService<ArchivoHeader, Long> implements
		ArchivoHeaderService {

	@Value("${file.max.size}")
	private Long sizeMax;

	@Inject
	private ArchivoHeaderRepository repository;

	@Inject
	private ArchivoFacturaRepository archivoFacturaRepository;

	@Inject
	private ArchivoRepository archivoRepository;

	@Inject
	private ArchivoOrdenRepository archivoOrdenRepository;

	@Inject
	private PageConverter<ArchivoHeader> pageConverter;

	@Override
	protected JpaRepository<ArchivoHeader, Long> getRepository() {
		return repository;
	}

	@Override
	protected PageConverter<ArchivoHeader> getPageConverter() {
		return pageConverter;
	}

	public Boolean canDelete(Long idHeader) {
		return archivoFacturaRepository
				.findByArchivoHeaderIdAndFacturaNivelIsNotNull(idHeader) != null ? false
				: true;
	}

	@Override
	public boolean isUnique(String fileName, Long idFactura, Integer fileType, Boolean force) {
		boolean result = true;
		
		for (ArchivoFactura af : archivoFacturaRepository
				.findByFacturaIdAndArchivoHeaderDeleted(idFactura, false)) {

			if ((af.getArchivoHeader().getName().equalsIgnoreCase(fileName) && af.getArchivoHeader().getFileType().equals(fileType)) || 
					(ArchivoHeader.FACTURA.equals(fileType) 
							&& af.getArchivoHeader().getFileType().equals(fileType))) {
				if(force){
					af.getArchivoHeader().setDeleted(true);
					repository.save(af.getArchivoHeader());
				}
				result = false;
				break;
			}
		}
		return result;
	}

	@Override
	public Long getSizeMax() {
		return sizeMax;
	}

	@Override
	public ArchivoHeader saveFile(String fileName, String type, String idTmp,
			Integer fileType, Long idUsuario) {
		ArchivoHeader ah = new ArchivoHeader();
		ah.setName(fileName);
		ah.setFileType(fileType);
		ah.setType(type);
		ah.setIdTmp(idTmp);
		ah.setIdUsuario(idUsuario);
		ah.setDeleted(false);
		repository.save(ah);
		return ah;
	}

	@Override
	public ArchivoHeader updateFile(String fileName, String type, Long idOrden,
			Integer fileType, Long idUsuario) {
		ArchivoHeader ah = new ArchivoHeader();
		ah.setName(fileName);
		ah.setFileType(fileType);
		ah.setType(type);
		ah.setIdTmp(null);
		ah.setIdUsuario(idUsuario);
		ah.setDeleted(false);
		repository.save(ah);
		return ah;
	}

	@Override
	public List<ArchivoHeader> findByIdTmp(String idTmp) {
		return repository.findByIdTmp(idTmp);
	}

	@Override
	public void deleteByHeader(ArchivoHeader archivoHeader, Boolean force) {
		if (force) {
			archivoRepository.deleteArchivoByIdHeader(archivoHeader.getId());
			repository.delete(archivoHeader);
		} else {
			archivoHeader.setDeleted(true);
			repository.save(archivoHeader);
		}
	}
	
	public void deleteByTempId(String tempId) {
		if(repository.countHeaderByIdHeaderTemp(tempId)>0){
			archivoRepository.deleteArchivoByIdHeaderTemp(tempId);
			repository.deleteHeaderByIdHeaderTemp(tempId);
		}
	}
	

	@Override
	public boolean isUniqueFileOrden(String fileName, Integer fileType,
			Long idOrden) {
		boolean result = true;
		for (ArchivoOrden ao : archivoOrdenRepository
				.findByOrdenIdAndArchivoHeaderDeleted(idOrden, false)) {
			if (ao.getArchivoHeader().getName().equals(fileName)
					&& ao.getArchivoHeader().getFileType().equals(fileType)) {
				result = false;
				break;
			}
		}
		return result;
	}

	@Override
	public boolean isUniqueFileTmp(String fileName, Integer fileType,
			String idTmp) {
		boolean result = true;
		for (ArchivoHeader ah : repository.findByIdTmp(idTmp)) {
			if (ah.getName().equals(fileName)
					&& ah.getFileType().equals(fileType)) {
				result = false;
				break;
			}
		}
		return result;
	}

}