package com.vates.facpro.persistence.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.Factura;
import com.vates.facpro.persistence.domain.Nivel;
import com.vates.facpro.persistence.paging.PageConverter;
import com.vates.facpro.persistence.repository.FacturaRepository;
import com.vates.facpro.persistence.service.FacturaService;

/**
 * @author Cabrera Manuel
 * 
 */
@Repository("facturaService")
@Transactional(propagation = Propagation.NESTED)
public class FacturaServiceImpl extends AbstractBaseService<Factura, Long> implements
		FacturaService {

	@Inject
	private FacturaRepository repository;

	@Inject
	private PageConverter<Factura> pageConverter;
	
	@Override
	public void saveFactura(Factura factura) {
		repository.save(factura);		
	}
	
	@Override
	public void saveAndFlushFactura(Factura factura) {
		repository.saveAndFlush(factura);		
	}

	@Override
	protected JpaRepository<Factura, Long> getRepository() {
		return repository;
	}

	@Override
	protected PageConverter<Factura> getPageConverter() {
		return pageConverter;
	}
	
	
	

	@Override
	public Factura find(long id) {
		// TODO Auto-generated method stub
		return repository.findOne(id);
	}
	
	@Override
	public Factura findById(long id) {
		return repository.findById(id);
	}
	
	@Override
	public Factura findByAdmId(long id) {
		// TODO Auto-generated method stub
		return repository.findByFacturaAdmId(id);
	}
	
	public void updateFacturaNivel(Factura factura, Nivel nivel){
		factura.setNivel(nivel);
		repository.save(factura);
	}

	@Override
	public void flush() {
		repository.flush();
	}

	public String prepareString(String input) {
		StringBuffer result = new StringBuffer();
		result.append("%");
		result.append(input);
		result.append("%");
		return result.toString();
	}

	

//	private Pageable constructPageSpecification(int pageIndex, int rowsPerPage) {
//		Pageable pageSpecification = new PageRequest(pageIndex, rowsPerPage,
//				sortByNumeroDes());
//		return pageSpecification;
//	}

//	private Sort sortByNumeroDes() {
//		return new Sort(Sort.Direction.DESC, "numero");
//	}
	
}
