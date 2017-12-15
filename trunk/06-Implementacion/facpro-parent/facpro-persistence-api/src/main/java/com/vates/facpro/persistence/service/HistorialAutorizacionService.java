package com.vates.facpro.persistence.service;

import java.util.List;

import com.vates.facpro.persistence.domain.HistorialAutorizacion;

/**
 * @author
 * 
 */
public interface HistorialAutorizacionService extends
		BaseService<HistorialAutorizacion, Long> {

	List<HistorialAutorizacion> findByuserAndFactura(Long idFactura);

	void saveHistorial(Long idFac, String descripcion, Long estado, Long userId)
			throws Exception;

	void rechazarFacturaHistorial(Long idFac, String motivo, Long userId) throws Exception;

	void autorizarAnterioresHistorial(Long id, Long userId, String descripcion,
			Long estado) throws Exception;

	void rechazarAnterioresHistorial(Long id, Long userId, String motivo)
			throws Exception;

	void informar(Long idFactura, Long userId, String descripcion)
			throws Exception;

}
