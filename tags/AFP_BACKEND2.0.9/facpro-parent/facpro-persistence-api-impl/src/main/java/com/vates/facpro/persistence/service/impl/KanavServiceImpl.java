package com.vates.facpro.persistence.service.impl;

import javax.inject.Inject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.Kanav;
import com.vates.facpro.persistence.paging.PageConverter;
import com.vates.facpro.persistence.repository.KanavRepository;
import com.vates.facpro.persistence.service.KanavService;

/**
 * @author Cabrera Manuel
 * 
 */
@Repository("kanavService")
@Transactional(propagation = Propagation.NESTED)
public class KanavServiceImpl extends AbstractBaseService<Kanav, Long> implements
		KanavService {

	@Inject
	private KanavRepository repository;

	@Inject
	private PageConverter<Kanav> pageConverter;

	@Override
	public Kanav findById(long id) {
		return repository.findById(id);
	}

	@Override
	protected JpaRepository<Kanav, Long> getRepository() {
		return repository;
	}

	@Override
	protected PageConverter<Kanav> getPageConverter() {
		return pageConverter;
	}
		
}