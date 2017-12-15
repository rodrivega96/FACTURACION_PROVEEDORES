package com.vates.facpro.persistence.service.impl;

import javax.inject.Inject;
import javax.persistence.OptimisticLockException;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.Archivo;
import com.vates.facpro.persistence.domain.ArchivoHeader;
import com.vates.facpro.persistence.paging.PageConverter;
import com.vates.facpro.persistence.repository.ArchivoHeaderRepository;
import com.vates.facpro.persistence.repository.ArchivoRepository;
import com.vates.facpro.persistence.service.ArchivoService;

/**
 * @author
 * 
 */
@Repository("ArchivoService")
@Transactional(propagation = Propagation.NESTED)
public class ArchivoServiceImpl extends AbstractBaseService<Archivo, Long>
		implements ArchivoService {

	@Inject
	private ArchivoRepository repository;

	@Inject
	private ArchivoHeaderRepository archivoHeaderRepository;

	@Inject
	private PageConverter<Archivo> pageConverter;

	@Override
	protected JpaRepository<Archivo, Long> getRepository() {
		return repository;
	}

	@Override
	protected PageConverter<Archivo> getPageConverter() {
		return pageConverter;
	}

	@Override
	public Archivo findByHeaderId(Long idHeader) {
		return repository.findByArchivoHeaderId(idHeader);
	}

	@Override
	public void saveFile(byte[] file, ArchivoHeader archivoHeader) {
		Archivo archivo = new Archivo();
		archivo.setContenidoBlob(file);
		archivo.setArchivoHeader(archivoHeader);
		repository.saveAndFlush(archivo);
	}

	@Override
	public void deleteFile(Long idArchivoHeader) {
		ArchivoHeader ah = archivoHeaderRepository.findOne(idArchivoHeader);
		if (ah.getDeleted()) {
			throw new OptimisticLockException();
		}
		ah.setDeleted(true);
		archivoHeaderRepository.save(ah);
	}

}