package com.vates.facpro.service.web.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.OptimisticLockException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.vates.facpro.business.states.oc.AdministradorEstadosOC;
import com.vates.facpro.business.states.oc.EstadoOC;
import com.vates.facpro.business.states.oc.EstadosOC;
import com.vates.facpro.persistence.domain.ArchivoHeader;
import com.vates.facpro.persistence.domain.OcProyecto;
import com.vates.facpro.persistence.domain.Orden;
import com.vates.facpro.persistence.domain.OrdenView;
import com.vates.facpro.persistence.domain.Proyecto;
import com.vates.facpro.persistence.domain.TipoOrden;
import com.vates.facpro.persistence.paging.Page;
import com.vates.facpro.persistence.service.ArchivoHeaderService;
import com.vates.facpro.persistence.service.ArchivoOrdenService;
import com.vates.facpro.persistence.service.OrdenService;
import com.vates.facpro.security.dto.UserDTO;
import com.vates.facpro.service.web.comparators.ProyectoDTOComparator;
import com.vates.facpro.service.web.dto.ArchivoOrdenDTO;
import com.vates.facpro.service.web.dto.ConsultarOrdenDTO;
import com.vates.facpro.service.web.dto.ConversorDTO;
import com.vates.facpro.service.web.dto.DatoInicialOrdenDTO;
import com.vates.facpro.service.web.dto.EstadoOrdenDTO;
import com.vates.facpro.service.web.dto.FacturaClientePorOCDTO;
import com.vates.facpro.service.web.dto.OrdenCompraDTO;
import com.vates.facpro.service.web.dto.OrdenResponseDTO;
import com.vates.facpro.service.web.dto.OrdenesResponseDTO;
import com.vates.facpro.service.web.dto.PrefaResponseDTO;
import com.vates.facpro.service.web.dto.PropuestasResponseDTO;
import com.vates.facpro.service.web.dto.ProyectoDTO;
import com.vates.facpro.service.web.dto.ProyectosResponseDTO;

@Service("facpro.service.orden")
@Path("/orden")
public class OrdenWebService {

	private static final int STATUS_OK = 0;
	private static final int STATUS_OPTIMISTIC_EX = 1;
	private static final int STATUS_DATABASE_EXCEPTION = 2;
	private static final int STATUS_EXIST_OC = 3;
	private static final int STATUS_NOT_FILE = 4;
	private static final int STATUS_CHANGE_STATE = 5;

	@Inject
	private OrdenService ordenService;

	@Inject
	private ArchivoHeaderService archivoHeaderService;

	@Inject
	private ArchivoOrdenService archivoOrdenService;

	@Inject
	private CacheManager cacheManager;

	@SuppressWarnings("finally")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/saveOrden")
	@Consumes(MediaType.APPLICATION_JSON)
	public OrdenResponseDTO saveOrden(OrdenCompraDTO dto,
			@HeaderParam(value = "vates-session-id") String sessionId) {
		OrdenResponseDTO response = new OrdenResponseDTO();
		Orden oc = null;
		try {
			UserDTO userDTO = (UserDTO) this.cacheManager
					.getCache("defaultCache").get(sessionId).get();

			if (dto.getTipo().equals(TipoOrden.INTERNA)
					|| ordenService.esUnica(dto.getNumero(), dto.getTipo())) {
				oc = new Orden();
				ConversorDTO.getOrden(dto, oc, userDTO, false);		
			if(dto.getTipo().equals(TipoOrden.INTERNA)){
				ordenService.setNumeroOCInterna(oc);
			}
			oc = ordenService.saveOrden(oc);
			response.setNumeroInterna(oc.getNumero());
			if (!dto.getArchivos().isEmpty()) {
				for (ArchivoOrdenDTO archDTO : dto.getArchivos()) {
					archivoOrdenService.createArchivosOrden(archDTO
							.getId(), oc);
				}
			}
			archivoHeaderService.deleteByTempId(dto
					.getIdTmp());
			response.setStatus(STATUS_OK);
			} else {
				response.setStatus(STATUS_EXIST_OC);
			}
			response.setMessage("");
		} catch (OptimisticLockException e) {
			if(oc!=null && oc.getId()!=null){
				ordenService.delete(oc);
				archivoOrdenService.deletebyOcId(oc.getId());
			}
			response.setMessage(e.getMessage());
			response.setStatus(STATUS_OPTIMISTIC_EX);
		} catch (Exception ex) {
			if(oc!=null && oc.getId()!=null){
				ordenService.delete(oc);
				archivoOrdenService.deletebyOcId(oc.getId());
			}
			response.setMessage(ex.getMessage());
			response.setStatus(STATUS_DATABASE_EXCEPTION);
		} finally {
			return response;
		}
	}

	@SuppressWarnings("finally")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/updateOrden")
	@Consumes(MediaType.APPLICATION_JSON)
	public OrdenResponseDTO updateOrden(OrdenCompraDTO dto,
			@HeaderParam(value = "vates-session-id") String sessionId) {
		OrdenResponseDTO response = new OrdenResponseDTO();
		try {
			UserDTO userDTO = (UserDTO) this.cacheManager
					.getCache("defaultCache").get(sessionId).get();
			Orden oc = ordenService.getOrden(dto.getId());
			if (AdministradorEstadosOC
					.getEstado(oc)
					.getProximosEstados()
					.contains(
							AdministradorEstadosOC.estados.get(dto.getEstado()))) {
				ConversorDTO.getOrden(dto, oc, userDTO, false);
				oc = ordenService.updateOrden(oc, dto.getTotal(),
						dto.getEstado());
				response.setStatus(STATUS_OK);
			} else {
				response.setStatus(STATUS_CHANGE_STATE);
			}
			response.setMessage("");
		} catch (OptimisticLockException e) {
			response.setMessage(e.getMessage());
			response.setStatus(STATUS_OPTIMISTIC_EX);
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(STATUS_DATABASE_EXCEPTION);
		} finally {
			return response;
		}
	}
	
	@SuppressWarnings("finally")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/forceUpdateOrden")
	@Consumes(MediaType.APPLICATION_JSON)
	public OrdenResponseDTO forceUpdateOrden(OrdenCompraDTO dto,
			@HeaderParam(value = "vates-session-id") String sessionId) {
		OrdenResponseDTO response = new OrdenResponseDTO();
		try {
			UserDTO userDTO = (UserDTO) this.cacheManager
					.getCache("defaultCache").get(sessionId).get();
			Orden oc = ordenService.getOrden(dto.getId());
			ConversorDTO.getOrden(dto, oc, userDTO, true);
			oc = ordenService.updateOrden(oc, dto.getTotal(), dto.getEstado());
			response.setStatus(STATUS_OK);
			response.setMessage("");
		} catch (OptimisticLockException e) {
			response.setMessage(e.getMessage());
			response.setStatus(STATUS_OPTIMISTIC_EX);
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(STATUS_DATABASE_EXCEPTION);
		} finally {
			return response;
		}
	}

	@SuppressWarnings("finally")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getDatoInicial")
	@Consumes(MediaType.APPLICATION_JSON)
	public OrdenResponseDTO getDatoInicial() {
		OrdenResponseDTO response = new OrdenResponseDTO();
		try {
			DatoInicialOrdenDTO datoDTO = getDatosIniciales(AdministradorEstadosOC.estadosIniciales);
			response.setDatoInicialDTO(datoDTO);
			response.setStatus(STATUS_OK);
			response.setMessage("");
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(STATUS_DATABASE_EXCEPTION);
		} finally {
			return response;
		}
	}

	@SuppressWarnings("finally")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getOrdenById")
	@Consumes(MediaType.APPLICATION_JSON)
	public OrdenResponseDTO getOrdenById(final @QueryParam("id") Long id) {
		OrdenResponseDTO response = new OrdenResponseDTO();
		try {
			Orden orden = ordenService.getOrden(id);
			response.setDatoInicialDTO(getDatosIniciales(AdministradorEstadosOC.estados
					.get(orden.getEstado()).getProximosEstados()));
			response.setDto(ConversorDTO.getOrdenCompraDTO(orden));
			response.getDto().setProyectos(cargarProyectos(orden));
			response.setArchivosList(ConversorDTO
					.getArchivoOrden(archivoOrdenService.findByOrdenIdAndDelete(id, false), orden.getEstado()));
			response.setStatus(STATUS_OK);
			response.setMessage("");
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(STATUS_DATABASE_EXCEPTION);
		} finally {
			return response;
		}
	}
	
	private List<ProyectoDTO> cargarProyectos(Orden orden){
		List<ProyectoDTO> listProyectoDTO = new ArrayList<ProyectoDTO>();
		Map<Long, OcProyecto> proyectosIds = new HashMap<Long, OcProyecto>();
		for (OcProyecto ocProy : orden.getOcProyectos()) {
			proyectosIds.put(ocProy.getProyectoId(), ocProy);
		}
		for(Proyecto proy:ordenService.findProyectosByIds(proyectosIds.keySet())){
			ProyectoDTO proyectoDTO = ConversorDTO.getProyectoDTO(proy,
					proyectosIds.get(proy.getId()), orden.getEstado());
			listProyectoDTO.add(proyectoDTO);
		}
		Collections.sort(listProyectoDTO, new ProyectoDTOComparator());
		return listProyectoDTO;
	}

	@SuppressWarnings("finally")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getFiltroInicial")
	@Consumes(MediaType.APPLICATION_JSON)
	public OrdenesResponseDTO getFiltroInicial() {
		OrdenesResponseDTO response = new OrdenesResponseDTO();
		try {
			DatoInicialOrdenDTO datoDTO = new DatoInicialOrdenDTO();
			datoDTO.setEstados(ConversorDTO
					.getEstadosDTO(new HashSet<EstadoOC>(
							AdministradorEstadosOC.estados.values())));
			datoDTO.setClientes(ConversorDTO.getClienteDTO(ordenService
					.getClientes()));
			datoDTO.setEstadosPreFac(ConversorDTO
					.getEstadosPreFacDTO(ordenService.getEsatdosPreFac()));
			response.setDatoInicialOrdenDTO(datoDTO);
			response.setStatus(STATUS_OK);
			response.setMessage("");
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(STATUS_DATABASE_EXCEPTION);
		} finally {
			return response;
		}
	}
	
	@SuppressWarnings("finally")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getFiltroProyecto")
	@Consumes(MediaType.APPLICATION_JSON)
	public ProyectosResponseDTO getFiltroProyecto(
			final @QueryParam("nombre") String nombre,
			final @QueryParam("activo") String activo,
			final @QueryParam("seleccionado") String id,
			final @QueryParam("propuestaId") String propuestaId,
			final @QueryParam("clienteId") String clienteId,
			final @QueryParam("managerId") String managerId,
			final @QueryParam("pm") String pm) {
		ProyectosResponseDTO response = new ProyectosResponseDTO();
		try {
			response.setProyectosDTO(ConversorDTO.getProyectoDTO(ordenService
					.getProyectos(nombre, activo, pm, propuestaId, clienteId, managerId),
					id));
			response.setStatus(STATUS_OK);
			response.setMessage("");
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(STATUS_DATABASE_EXCEPTION);
		} finally {
			return response;
		}
	}

	@SuppressWarnings("finally")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getFiltroPropuesta")
	@Consumes(MediaType.APPLICATION_JSON)
	public PropuestasResponseDTO getFiltroPropuesta(
			final @QueryParam("descripcion") String descripcion,
			final @QueryParam("comercial") String comercial,
			final @QueryParam("fechaVigenciaDesde") String fechaVigenciaDesde,
			final @QueryParam("fechaVigenciaHasta") String fechaVigenciaHasta,
			final @QueryParam("clienteId") String clienteId,
			final @QueryParam("seleccionado") String id) {
		PropuestasResponseDTO response = new PropuestasResponseDTO();
		try {
			response.setPropuestasDTO(ConversorDTO.getPropuestaDTO(ordenService
					.getPropuestas(descripcion, comercial, fechaVigenciaDesde,
							fechaVigenciaHasta, clienteId), id));
			response.setStatus(STATUS_OK);
			response.setMessage("");
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(STATUS_DATABASE_EXCEPTION);
		} finally {
			return response;
		}
	}
	
	@SuppressWarnings("finally")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getFiltroManager")
	@Consumes(MediaType.APPLICATION_JSON)
	public ProyectosResponseDTO getFiltroManager(
			final @QueryParam("nombre") String nombre,
			final @QueryParam("seleccionado") String id) {
		ProyectosResponseDTO response = new ProyectosResponseDTO();
		try {
			response.setManagersDTO(ConversorDTO.getManagerDTO(
					ordenService.getManagers(nombre), id));
			response.setStatus(STATUS_OK);
			response.setMessage("");
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(STATUS_DATABASE_EXCEPTION);
		} finally {
			return response;
		}
	}
	
	private DatoInicialOrdenDTO getDatosIniciales(Set<EstadoOC> estados) {
		DatoInicialOrdenDTO datoDTO = new DatoInicialOrdenDTO();
		datoDTO.setSizeMax(archivoHeaderService.getSizeMax());
		datoDTO.setTipoList(ConversorDTO.getTipoOrdenesDTO(ordenService
				.getTiposOrdenes()));
		datoDTO.setUnidadList(ConversorDTO.getUnidadesDTO(ordenService
				.getUnidades()));
		datoDTO.setEstados(ConversorDTO.getEstadosDTO(estados));
		datoDTO.setMonedaList(ConversorDTO.getMonedaDTO(ordenService
				.getMonedas()));
		datoDTO.setClientes(ConversorDTO.getClienteDTO(ordenService
				.getClientes()));
		return datoDTO;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/ocPaginatedList")
	@Consumes(MediaType.APPLICATION_JSON)
	public Page<OrdenView> getPaginatedList(
			final @QueryParam("clienteId") String clienteId,
			final @QueryParam("clienteNombre") String clienteNombre,
			final @QueryParam("estado") String estado,
			final @QueryParam("page") int pageIndex,
			final @QueryParam("proyectoId") String proyectoId,
			final @QueryParam("propuestaId") String propuestaId,
			final @QueryParam("activa") String activa,
			final @QueryParam("limit") int rowsPerPage) {

		return ordenService.findPaginatedFilteredList(clienteNombre, clienteId,
				proyectoId, propuestaId, activa, estado, pageIndex - 1, rowsPerPage);

	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/ocPaginatedConsulList")
	@Consumes(MediaType.APPLICATION_JSON)
	public ConsultarOrdenDTO getPaginatedConsultList(
			final @QueryParam("clienteId") String clienteId,
			final @QueryParam("clienteNombre") String clienteNombre,
			final @QueryParam("estadoOC") String estadoOC,
			final @QueryParam("pageOC") int pageIndexOC,
			final @QueryParam("proyectoId") String proyectoId,
			final @QueryParam("managerId") String managerId,
			final @QueryParam("numero") String numero,
			final @QueryParam("fechaDesde") String fvDesde,
			final @QueryParam("fechaHasta") String fvHasta,
			final @QueryParam("limitOC") int rowsPerPageOC)  {

		ConsultarOrdenDTO dto = new ConsultarOrdenDTO();
		dto.setOrdenView(ordenService.findPaginatedFilteredConsultList(clienteNombre, clienteId, fvDesde, fvHasta,
				proyectoId, managerId, numero, estadoOC, pageIndexOC - 1, rowsPerPageOC));
		return dto;

	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/ocPaginatedPrefaConsulList")
	@Consumes(MediaType.APPLICATION_JSON)
	public ConsultarOrdenDTO getPaginatedPrefaConsultList(
			final @QueryParam("ocId") String ocId,
			final @QueryParam("estadoPreFac") String estadoPreFac,
			final @QueryParam("pagePF") int pageIndexPF,
			final @QueryParam("limitPF") int rowsPerPagePF)  {

		ConsultarOrdenDTO dto = new ConsultarOrdenDTO();
		dto.setPreFacView(ordenService.findPaginatedFilteredPreFacList(ocId,
				estadoPreFac, pageIndexPF - 1, rowsPerPagePF));
		return dto;

	}
	
	@SuppressWarnings("finally")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/deleteOrden")
	@Consumes(MediaType.APPLICATION_JSON)
	public OrdenResponseDTO deleteOrden(Long id,
			@HeaderParam(value = "vates-session-id") String sessionId) {
		OrdenResponseDTO response = new OrdenResponseDTO();
		try {
			UserDTO userDTO = (UserDTO) this.cacheManager
					.getCache("defaultCache").get(sessionId).get();
			Orden oc = ordenService.getOrden(id);
			oc.setUpdatedBy(userDTO.getId());
			oc.setActiva(0L);
			oc = ordenService.updateOrden(oc, oc.getTotal(), oc.getEstado());
			response.setStatus(STATUS_OK);
			response.setMessage("");
		} catch (OptimisticLockException e) {
			response.setMessage(e.getMessage());
			response.setStatus(STATUS_OPTIMISTIC_EX);
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(STATUS_DATABASE_EXCEPTION);
		} finally {
			return response;
		}
	}
	
	@SuppressWarnings("finally")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/changeStateOrden")
	@Consumes(MediaType.APPLICATION_JSON)
	public OrdenResponseDTO estadoOrden(EstadoOrdenDTO dto,
			@HeaderParam(value = "vates-session-id") String sessionId) {
		OrdenResponseDTO response = new OrdenResponseDTO();
		try {
			Orden oc = ordenService.getOrden(dto.getId());
			if (oc.getEstado().equals(dto.getEstadoId())) {
				if (oc.getTipo().equals(Orden.INTERNA)
						|| (oc.getTipo().equals(Orden.EXTERNA) && archivoOrdenService
								.countByOrdenIdAndDeletedAndFileType(
										dto.getId(), false, ArchivoHeader.OC) > 0)) {
					UserDTO userDTO = (UserDTO) this.cacheManager
							.getCache("defaultCache").get(sessionId).get();
					oc.setUpdatedBy(userDTO.getId());
					oc = ordenService.updateOrdenChangeState(oc,
							userDTO.getId(), null);
					response.setStatus(STATUS_OK);
				} else {
					response.setStatus(STATUS_NOT_FILE);
				}
			} else {
				response.setStatus(STATUS_CHANGE_STATE);
			}
			response.setMessage("");
		} catch (OptimisticLockException e) {
			response.setMessage(e.getMessage());
			response.setStatus(STATUS_OPTIMISTIC_EX);
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(STATUS_DATABASE_EXCEPTION);
		} finally {
			return response;
		}
	}
	
	@SuppressWarnings("finally")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/closeOrden")
	@Consumes(MediaType.APPLICATION_JSON)
	public OrdenResponseDTO closeOrden(EstadoOrdenDTO dto,
			@HeaderParam(value = "vates-session-id") String sessionId) {
		OrdenResponseDTO response = new OrdenResponseDTO();
		try {
			Orden oc = ordenService.getOrden(dto.getId());
			if (oc.getEstado().equals(dto.getEstadoId())) {
				UserDTO userDTO = (UserDTO) this.cacheManager
						.getCache("defaultCache").get(sessionId).get();
				oc.setUpdatedBy(userDTO.getId());
				oc = ordenService.updateOrdenChangeState(oc, userDTO.getId(),
						EstadosOC.CERRADA);
				response.setStatus(STATUS_OK);
			} else {
				response.setStatus(STATUS_CHANGE_STATE);
			}
			response.setMessage("");
		} catch (OptimisticLockException e) {
			response.setMessage(e.getMessage());
			response.setStatus(STATUS_OPTIMISTIC_EX);
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(STATUS_DATABASE_EXCEPTION);
		} finally {
			return response;
		}
	}
	
	@SuppressWarnings("finally")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getArchivoByOrdenId")
	@Consumes(MediaType.APPLICATION_JSON)
	public OrdenResponseDTO getArchivoByOrdenId(final @QueryParam("id") Long id) {
		OrdenResponseDTO response = new OrdenResponseDTO();
		try {
			response.setArchivosList(ConversorDTO
					.getArchivoOrden(archivoOrdenService
							.findByOrdenIdAndDelete(id, false)));
			response.setStatus(STATUS_OK);
			response.setMessage("");
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(STATUS_DATABASE_EXCEPTION);
		} finally {
			return response;
		}
	}
	
	@SuppressWarnings("finally")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getFacturaClienteByOrdenId")
	@Consumes(MediaType.APPLICATION_JSON)
	public OrdenResponseDTO getFacturaClienteByOrdenId(
			final @QueryParam("id") Long ordenId) {
		OrdenResponseDTO response = new OrdenResponseDTO();
		try {
			response.setFacturaList(ConversorDTO.getFacturasList(ordenService.findFacturasByOrdenId(ordenId)));
			response.setStatus(STATUS_OK);
			response.setMessage("");
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(STATUS_DATABASE_EXCEPTION);
		} finally {
			return response;
		}
	}
	
	@SuppressWarnings("finally")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getColaboradoresOCs")
	@Consumes(MediaType.APPLICATION_JSON)
	public PrefaResponseDTO getColaboradoresOCs(final @QueryParam("id") Long id) {
		PrefaResponseDTO response = new PrefaResponseDTO();
		try {
			response.setOcs(ordenService.getOrdenesPrefByPrefa(id));
			response.setPeoples(ConversorDTO.getPeoples(ordenService
					.getPeoplesByPrefa(id)));
			response.setStatus(STATUS_OK);
			response.setMessage("");
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(STATUS_DATABASE_EXCEPTION);
		} finally {
			return response;
		}
	}
	
	@SuppressWarnings("finally")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getFacturasByOrdenId")
	@Consumes(MediaType.APPLICATION_JSON)
	public OrdenResponseDTO getFacturasByOrdenId(
			final @QueryParam("id") Long ordenId,
			final @QueryParam("clienteId") Long clienteId) {
		OrdenResponseDTO response = new OrdenResponseDTO();
		try {
			response.setFacturaClientePorOCDTO(ConversorDTO
					.getFacturasByOrdenId(ordenService
							.findFacturaPorOCByOrdenId(ordenId), ordenService
							.findFacturasByClienteId(clienteId, ordenId),
							ordenId));
			response.setStatus(STATUS_OK);
			response.setMessage("");
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(STATUS_DATABASE_EXCEPTION);
		} finally {
			return response;
		}
	}
	
	@SuppressWarnings("finally")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/saveAsociarFacturas")
	@Consumes(MediaType.APPLICATION_JSON)
	public OrdenResponseDTO saveAsociarFacturas(FacturaClientePorOCDTO dto,
			@HeaderParam(value = "vates-session-id") String sessionId) {
		OrdenResponseDTO response = new OrdenResponseDTO();
		try {
			UserDTO userDTO = (UserDTO) this.cacheManager
					.getCache("defaultCache").get(sessionId).get();
			ordenService.saveAsociarFactura(
					ConversorDTO.getFacturaPorOC(dto.getFacturaCliente(),
							dto.getOrdenId(), userDTO.getId()), dto.getOrdenId());
			response.setStatus(STATUS_OK);
			response.setMessage("");
		} catch (OptimisticLockException e) {
			response.setMessage(e.getMessage());
			response.setStatus(STATUS_OPTIMISTIC_EX);
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(STATUS_DATABASE_EXCEPTION);
		} finally {
			return response;
		}
	}
}
