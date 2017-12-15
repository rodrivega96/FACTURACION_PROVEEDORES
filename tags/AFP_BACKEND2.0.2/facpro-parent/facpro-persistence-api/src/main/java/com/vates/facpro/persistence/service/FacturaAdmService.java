package com.vates.facpro.persistence.service;

import java.util.Set;

import com.vates.facpro.persistence.domain.FacturaAdm;
import com.vates.facpro.persistence.domain.FacturaView;
import com.vates.facpro.persistence.paging.Page;

/**
 * @author Manuel Cabrera
 * 
 */
public interface FacturaAdmService extends BaseService<FacturaAdm, Long> {

	FacturaAdm find(long id);

	Page<FacturaView> findPaginatedFilteredList(String numero, String tipo,
			String cuit, String razon, String cc, String centroc, String wf,
			String fvDesde, String fiDesde, String flfDesde, String fvHasta,
			String fiHasta, String flfHasta, String ffacDesde, String ffacHasta, String estado, int pageIndex,
			int rowsPerPage, Boolean all, Long userId);

	Page<FacturaView> findPaginatedFilteredList(Long userId, int pageIndex,
			int rowsPerPage, String variable, String variableLocal, Boolean order);
	
	Page<FacturaView> findPaginatedPendingFilteredList(Long userId, int pageIndex,
			int rowsPerPage, String variable, String variableLocal, Boolean order);
	
	void saveFactura(FacturaAdm factura);
	
	FacturaAdm findById(Long id);
	
	Set<String> findJerarquiasExcluidas();

}