package com.vates.facpro.persistence.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.NivelPublicado;
import com.vates.facpro.persistence.paging.PageConverter;
import com.vates.facpro.persistence.repository.NivelPublicadoRepository;
import com.vates.facpro.persistence.service.NivelPublicadoService;

/**
 * @author cparodi
 * 
 */
@Repository("nivelPublicadoService")
@Transactional(propagation = Propagation.NESTED)
public class NivelPublicadoServiceImpl extends
		AbstractBaseService<NivelPublicado, Long> implements
		NivelPublicadoService {

	@Inject
	private NivelPublicadoRepository repository;


	@Override
	public NivelPublicado saveNivelPublicado(NivelPublicado nivelPublicado) {
		return repository.saveAndFlush(nivelPublicado);
	}


	@Override
	public List<NivelPublicado> findFacturaByLast(boolean last) {
		return repository.findFacturaByLast(last);
	}

	@Override
	protected JpaRepository<NivelPublicado, Long> getRepository() {
		return repository;
	}

	@Override
	public NivelPublicado find(Long id) {
		return repository.findOne(id);
	}

	@Override
	public NivelPublicado findNivelPublicadoByFacturaIdAndLast(Long id) {
		return repository.findNivelPublicadoByFacturaIdAndLast(id, true);
	}
	
	@Override
	public Long countNivelesByFacturaIdAndLastAndNivelEstadoNotIn(Long id, List<Integer> estados) {
		return repository.countNivelesByFacturaIdAndLastAndNivelEstadoNotIn(id, true, estados);
	}

	@Override
	protected PageConverter<NivelPublicado> getPageConverter() {
		return null;
	}


	@Override
	public List<NivelPublicado> findByFacturaId(Long id) {
		return repository.findByFacturaId(id);
	}

}
