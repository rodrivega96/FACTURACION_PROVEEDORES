package com.vates.facpro.persistence.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.ArchivoFactura;
import com.vates.facpro.persistence.domain.ArchivoHeader;
import com.vates.facpro.persistence.paging.PageConverter;
import com.vates.facpro.persistence.repository.ArchivoFacturaRepository;
import com.vates.facpro.persistence.repository.FacturaRepository;
import com.vates.facpro.persistence.service.ArchivoFacturaService;

/**
 * @author
 * 
 */
@Repository("ArchivoFacturaService")
@Transactional(propagation = Propagation.NESTED)
public class ArchivoFacturaServiceImpl extends
		AbstractBaseService<ArchivoFactura, Long> implements
		ArchivoFacturaService {

	@Inject
	private ArchivoFacturaRepository repository;

	@Inject
	private FacturaRepository facturaRepository;

	@Inject
	private PageConverter<ArchivoFactura> pageConverter;

	@Override
	protected JpaRepository<ArchivoFactura, Long> getRepository() {
		return repository;
	}

	@Override
	protected PageConverter<ArchivoFactura> getPageConverter() {
		return pageConverter;
	}

	@Override
	public ArchivoFactura saveFile(String fileName, String type, Long idFactura, Integer fileType, Long idUsuario) {
		ArchivoHeader header = new ArchivoHeader();
		ArchivoFactura af = new ArchivoFactura();
		header.setName(fileName);
		header.setType(type);
		header.setFileType(fileType);
		header.setIdUsuario(idUsuario);
		header.setDeleted(false);
		af.setFactura(facturaRepository.findOne(idFactura));
		af.setArchivoHeader(header);
		repository.save(af);
		return af;
	}

	@Override
	public List<ArchivoFactura> findByFacturaId(Long facturaId) {
		return repository.findByFacturaId(facturaId);
	}
	
	@Override
	public Long findArchivoHeaderIdByFacturaIdAndArchivoHeaderFileTypeAndArchivoHeaderDeleted(
			Long facturaId, Integer type, Boolean deleted) {
		return repository
				.findArchivoHeaderIdByFacturaIdAndArchivoHeaderFileTypeAndArchivoHeaderDeleted(
						facturaId, type, deleted);
	}

	@Override
	public boolean canDelete(Long facturaId) {
		return repository.findByFacturaIdAndFacturaNivelIsNotNull(facturaId).size() > 0 ? false
				: true;
	}
}