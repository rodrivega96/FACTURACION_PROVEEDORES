package com.vates.facpro.persistence.service;

import java.util.List;
import java.util.Set;

import com.vates.facpro.persistence.domain.FacturaAdm;
import com.vates.facpro.persistence.domain.FacturaAsientoAdm;
import com.vates.facpro.persistence.domain.FacturaView;
import com.vates.facpro.persistence.domain.NivelView;
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
			String fiHasta, String flfHasta, String ffacDesde,
			String ffacHasta, String userSelected, String estado,
			int pageIndex, int rowsPerPage, Boolean all, Long userId);
	
	Page<FacturaView> findPaginatedAdminFilteredList(String numero, String tipo,
			String cuit, String razon, String cc, String centroc, String wf,
			String fvDesde, String fiDesde, String flfDesde, String fvHasta,
			String fiHasta, String flfHasta, String ffacDesde, String ffacHasta, String estado, int pageIndex,
			int rowsPerPage, Boolean all, Long userId);

	
	Page<FacturaView> findPaginatedFilteredListAuth(String numero,
			String tipo, String cuit, String razon, String cc, String centroc,
			String fvDesde, String fiDesde, String flfDesde,
			String fvHasta, String fiHasta, String flfHasta, String ffacDesde,
			String ffacHasta,Long userId, int pageIndex,
			int rowsPerPage, String variable, String variableLocal, Boolean order);
	
	Page<FacturaView> findPaginatedPendingFilteredListLowLevel(String numero,
			String tipo, String cuit, String razon, String cc, String centroc,
			String fvDesde, String fiDesde, String flfDesde,
			String fvHasta, String fiHasta, String flfHasta, String ffacDesde,
			String ffacHasta,Long userId, int pageIndex,
			int rowsPerPage, String variable, String variableLocal, Boolean order, String userSelected);
	
	void saveFactura(FacturaAdm factura);
	
	FacturaAdm findById(Long id);
	
	Set<String> findJerarquiasExcluidas();
	
	List<FacturaAsientoAdm> getFilteredFacturaAsiento(List<FacturaAsientoAdm> listAsientos);

	List<NivelView> getPendingOk(Long id, Long userId);

}
