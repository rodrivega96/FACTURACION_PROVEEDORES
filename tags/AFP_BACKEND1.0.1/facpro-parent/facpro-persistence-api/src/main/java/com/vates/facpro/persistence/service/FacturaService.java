package com.vates.facpro.persistence.service;

import com.vates.facpro.persistence.domain.Factura;
import com.vates.facpro.persistence.domain.Nivel;

/**
 * @author Manuel Cabrera
 * 
 */
public interface FacturaService extends BaseService<Factura, Long> {

	
	Factura find(long id);
	
	Factura findById(long id);
	
	public Factura findByAdmId(long id);
	
//	Page<Factura> findPaginatedFilteredList(String numero, Date desde, Date hasta
//			, String tipo, int pageIndex, int rowsPerPage);
	
	void saveFactura(Factura factura);
	
	void saveAndFlushFactura(Factura factura);
	
	void flush();
	
	void updateFacturaNivel(Factura factura, Nivel nivel);

}
