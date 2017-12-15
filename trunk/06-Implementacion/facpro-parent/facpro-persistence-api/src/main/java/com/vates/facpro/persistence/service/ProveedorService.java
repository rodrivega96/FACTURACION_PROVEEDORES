package com.vates.facpro.persistence.service;

import java.util.List;

import com.vates.facpro.persistence.domain.MedioPago;
import com.vates.facpro.persistence.domain.Proveedor;
import com.vates.facpro.persistence.domain.ProveedorView;
import com.vates.facpro.persistence.domain.TipoTelefono;
import com.vates.facpro.persistence.paging.Page;

/**
 * @author Manuel Cabrera
 * 
 */
public interface ProveedorService extends BaseService<Proveedor, Long> {

	void deleteProveedor(long id);
	
	Proveedor findById(long id);
	
	Proveedor saveProveedor(Proveedor proveedpr);
	
	Proveedor saveProveedorView(ProveedorView proveedorView, Long userId);
	
	Proveedor saveAndFlushProveedor(Proveedor proveedor);
	
	void flush();
	
	ProveedorView findViewById(long id);
	
	Page<ProveedorView> find(String razonSocial, String cuit, String medioPago,
			int pageIndex, int rowsPerPage);
	
	List<TipoTelefono> getTiposTelefono();
	
	List<MedioPago> getMediosPago();
}
