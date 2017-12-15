package com.vates.facpro.persistence.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vates.facpro.persistence.domain.Cliente;
import com.vates.facpro.persistence.domain.EstadoPrefa;
import com.vates.facpro.persistence.domain.FacturaCliente;
import com.vates.facpro.persistence.domain.FacturasClientePorOC;
import com.vates.facpro.persistence.domain.Manager;
import com.vates.facpro.persistence.domain.Moneda;
import com.vates.facpro.persistence.domain.Orden;
import com.vates.facpro.persistence.domain.OrdenPrefView;
import com.vates.facpro.persistence.domain.OrdenView;
import com.vates.facpro.persistence.domain.People;
import com.vates.facpro.persistence.domain.PreFacturacionView;
import com.vates.facpro.persistence.domain.Propuesta;
import com.vates.facpro.persistence.domain.Proyecto;
import com.vates.facpro.persistence.domain.TipoOrden;
import com.vates.facpro.persistence.domain.Unidad;
import com.vates.facpro.persistence.paging.Page;

/**
 * @author Lucas Scarlatta
 * 
 */
public interface OrdenService extends BaseService<Orden, Long> {

	Orden saveOrden(Orden orden);
	
	Orden updateOrden(Orden orden, Double total, Long state);

	List<TipoOrden> getTiposOrdenes();

	List<Unidad> getUnidades();
	
	Orden getOrden(Long ordenId);
	
	List<Cliente> getClientes();
	
	List<Moneda> getMonedas();
	
	void setNumeroOCInterna(Orden orden);
	
	List<Proyecto> getProyectos(String nombre, String activo, String pm,
			String propuestaId, String clienteId, String managerId);
	
	List<Propuesta> getPropuestas(String descripcion, String comercial,
				String fechaVigenciaDesde, String fechaVigenciaHasta, String clienteId);
	
	List<Manager> getManagers(String nombre);

	Proyecto findProyectoById(Long proyectoId);
	
	Page<OrdenView>  findPaginatedFilteredList(String clienteNombre, String clienteId,
			String proyectoId, String propuestaId, String activa, String estado, 
			int pageIndex, int rowsPerPage);

	Boolean esUnica(String numero, Long tipo);
	
	Orden updateOrdenChangeState(Orden orden, Long userId, Long estado);

	Page<OrdenView> findPaginatedFilteredConsultList(String clienteNombre, String clienteId,
			String fvDesde, String fvHasta, String proyectoId, String managerId, String numero,
			String estadoOC, int pageIndex, int rowsPerPage);

	Page<PreFacturacionView> findPaginatedFilteredPreFacList(
			String ocId, String estadoPreFac, int pageIndex, int rowsPerPage);

	List<EstadoPrefa> getEsatdosPreFac();

	List<FacturaCliente> findFacturasByOrdenId(Long ordenId);

	List<People> getPeoplesByPrefa(Long prefaId);
	
	List<OrdenPrefView> getOrdenesPrefByPrefa(Long prefaId);

	List<Long> findFacturaPorOCByOrdenId(Long ordenId);
	
	List<Proyecto> findProyectosByIds(Set<Long> proyectoId);

	List<FacturaCliente> findFacturasByClienteId(Long clienteId, Long idOrden);

	void saveAsociarFactura(Map<Integer, Map<Long, FacturasClientePorOC>> map,
			Long ordenId, Long estado);

}
