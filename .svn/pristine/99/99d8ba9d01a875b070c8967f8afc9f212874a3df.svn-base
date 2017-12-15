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
	
	Factura saveFactura(Factura factura);
	
	Factura saveAndFlushFactura(Factura factura);
	
	void flush();
	
	void updateFacturaNivel(Factura factura, Nivel nivel);
	
	boolean canOverWriteFile(Long facturaId);

}
