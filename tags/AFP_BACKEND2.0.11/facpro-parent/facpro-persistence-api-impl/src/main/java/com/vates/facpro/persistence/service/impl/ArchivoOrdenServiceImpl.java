package com.vates.facpro.persistence.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.ArchivoHeader;
import com.vates.facpro.persistence.domain.ArchivoOrden;
import com.vates.facpro.persistence.domain.Orden;
import com.vates.facpro.persistence.paging.PageConverter;
import com.vates.facpro.persistence.repository.ArchivoHeaderRepository;
import com.vates.facpro.persistence.repository.ArchivoOrdenRepository;
import com.vates.facpro.persistence.repository.ArchivoRepository;
import com.vates.facpro.persistence.repository.OrdenRepository;
import com.vates.facpro.persistence.service.ArchivoOrdenService;

/**
 * @author
 * 
 */
@Repository("ArchivoOrdenService")
@Transactional(propagation = Propagation.NESTED)
public class ArchivoOrdenServiceImpl extends
		AbstractBaseService<ArchivoOrden, Long> implements ArchivoOrdenService {

	@Inject
	private ArchivoOrdenRepository repository;
	
	@Inject
	private ArchivoHeaderRepository archivoHeaderRepository;
	
	@Inject
	private ArchivoRepository archivoRepository;

	@Inject 
	private OrdenRepository ordenRepository;
	
	@Inject
	private PageConverter<ArchivoOrden> pageConverter;

	@Override
	protected JpaRepository<ArchivoOrden, Long> getRepository() {
		return repository;
	}

	@Override
	protected PageConverter<ArchivoOrden> getPageConverter() {
		return pageConverter;
	}

	@Override
	public void saveAll(List<ArchivoOrden> archivosOrden) {
		repository.save(archivosOrden);
	}
	
	public void createArchivosOrden(Long archivoHeaderId, Orden orden){
			ArchivoOrden aOrden = new ArchivoOrden();
			ArchivoHeader aHeader = archivoHeaderRepository.findById(archivoHeaderId);
			aHeader.setIdTmp(null);
			aOrden.setArchivoHeader(aHeader);
			aOrden.setOrden(orden);
			repository.save(aOrden);
	}
	
	public void deletebyOcId(Long id){
		for(ArchivoOrden ao: repository.findByOrdenId(id)){
			archivoRepository.deleteArchivoByIdHeader(ao.getArchivoHeader().getId());
			archivoHeaderRepository.delete(ao.getArchivoHeader().getId());
			repository.delete(ao.getId());
		}
	}

	@Override
	public List<ArchivoOrden> findByOrdenIdAndDelete(Long ordenId, Boolean delete) {
		return repository.findByOrdenIdAndArchivoHeaderDeleted(ordenId, delete);
	}

	@Override
	public ArchivoOrden updateFile(String fileName, String type, Long idOrden,
			Integer fileType, Long userId) {
		ArchivoHeader ah = new ArchivoHeader();
		ArchivoOrden ao = new ArchivoOrden();
		ah.setDeleted(false);
		ah.setFileType(fileType);
		ah.setIdUsuario(userId);
		ah.setName(fileName);
		ah.setType(type);
		ah.setIdTmp(null);
		ao.setArchivoHeader(ah);
		ao.setOrden(ordenRepository.findById(idOrden));
		repository.save(ao);
		return ao;
	}

	@Override
	public Long findArchivoHeaderIdByOrdenIdAndArchivoHeaderFileTypeAndArchivoHeaderDeleted(
			Long ordenId, Integer type, Boolean deleted) {
		return repository
				.findArchivoHeaderIdByFacturaIdAndArchivoHeaderFileTypeAndArchivoHeaderDeleted(
						ordenId, type, deleted);
	}

	@Override
	public Long countByOrdenIdAndDeletedAndFileType(Long ordenId,
			Boolean deleted, Integer fileType) {
		return repository.countByOrdenIdAndDeletedAndFileType(ordenId, deleted,
				fileType);
	}

}