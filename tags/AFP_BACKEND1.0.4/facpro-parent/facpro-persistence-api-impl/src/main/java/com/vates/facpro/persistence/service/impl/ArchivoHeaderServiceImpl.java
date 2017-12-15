package com.vates.facpro.persistence.service.impl;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.ArchivoFactura;
import com.vates.facpro.persistence.domain.ArchivoHeader;
import com.vates.facpro.persistence.paging.PageConverter;
import com.vates.facpro.persistence.repository.ArchivoFacturaRepository;
import com.vates.facpro.persistence.repository.ArchivoHeaderRepository;
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

}