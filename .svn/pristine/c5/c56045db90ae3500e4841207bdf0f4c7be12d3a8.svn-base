package com.vates.facpro.service.web.impl;

import java.util.HashSet;
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

import com.vates.facpro.business.states.factura.AdministradorEstados;
import com.vates.facpro.business.states.factura.Estados;
import com.vates.facpro.business.states.factura.Inicial;
import com.vates.facpro.persistence.domain.Factura;
import com.vates.facpro.persistence.domain.FacturaAdm;
import com.vates.facpro.persistence.domain.FacturaAsientoAdm;
import com.vates.facpro.persistence.domain.FacturaView;
import com.vates.facpro.persistence.paging.Page;
import com.vates.facpro.persistence.service.FacturaAdmService;
import com.vates.facpro.persistence.service.FacturaService;
import com.vates.facpro.persistence.util.ParseoService;
import com.vates.facpro.security.dto.UserDTO;
import com.vates.facpro.service.web.dto.AsientoFullDTO;
import com.vates.facpro.service.web.dto.FacturaFullDTO;
import com.vates.facpro.service.web.dto.FacturaResponseDTO;

@Service("facpro.service.factura")
@Path("/factura")
public class FacturaWebService {

	private static final int STATUS_OK = 0;
	private static final int STATUS_OPTIMISTIC_EX = 1;
	private static final int STATUS_DATABASE_EXCEPTION = 2;

	@Inject
	private FacturaService facturaService;

	@Inject
	private FacturaAdmService facturaAdmService;
	
	@Inject
	private CacheManager cacheManager;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/facturaPaginatedList")
	@Consumes(MediaType.APPLICATION_JSON)
	public Page<FacturaView> getPaginatedList(
			final @QueryParam("numero") String numero,
			final @QueryParam("tipo") String tipo,
			final @QueryParam("cuit") String cuit,
			final @QueryParam("razon") String razon,
			final @QueryParam("cc") String cc,
			final @QueryParam("centroc") String centroc,
			final @QueryParam("wf") String wf,
			final @QueryParam("fvDesde") String fvDesde,
			final @QueryParam("fiDesde") String fiDesde,
			final @QueryParam("flfDesde") String flfDesde,
			final @QueryParam("fvHasta") String fvHasta,
			final @QueryParam("fiHasta") String fiHasta,
			final @QueryParam("flfHasta") String flfHasta,
			final @QueryParam("ffacDesde") String ffacDesde,
			final @QueryParam("ffacHasta") String ffacHasta,
			final @QueryParam("estado") String estado,
			final @QueryParam("page") int pageIndex,
			final @QueryParam("limit") int rowsPerPage) {

		return facturaAdmService.findPaginatedFilteredList(numero, tipo, cuit,
				razon, cc, centroc, wf, fvDesde, fiDesde, flfDesde, fvHasta,
				fiHasta, flfHasta, ffacDesde,ffacHasta ,estado, pageIndex - 1, rowsPerPage, true, null);

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/facturaViewPaginatedList")
	@Consumes(MediaType.APPLICATION_JSON)
	public Page<FacturaView> getPaginatedList(
			@HeaderParam(value = "vates-session-id") String sessionId,
			final @QueryParam("page") int pageIndex,
			final @QueryParam("limit") int rowsPerPage,
			final @QueryParam("variable") String variable,
			final @QueryParam("varlocal") String variableLocal,
			final @QueryParam("order") Boolean order) {

		UserDTO userDTO = (UserDTO) this.cacheManager.getCache("defaultCache").get(sessionId).get();
		return facturaAdmService.findPaginatedFilteredList(userDTO.getId(),
				pageIndex - 1, rowsPerPage, variable, variableLocal, order);

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/nivelInferiorList")
	@Consumes(MediaType.APPLICATION_JSON)
	public Page<FacturaView> getPaginatedNivelInferiorList(
			@HeaderParam(value = "vates-session-id") String sessionId,
			final @QueryParam("page") int pageIndex,
			final @QueryParam("limit") int rowsPerPage,
			final @QueryParam("variable") String variable,
			final @QueryParam("varlocal") String variableLocal,
			final @QueryParam("order") Boolean order) {

		UserDTO userDTO = (UserDTO) this.cacheManager.getCache("defaultCache").get(sessionId).get();
		return facturaAdmService.findPaginatedPendingFilteredList(userDTO.getId(),
				pageIndex - 1, rowsPerPage, variable, variableLocal, order);
	}

	@SuppressWarnings("finally")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getFactura")
	@Consumes(MediaType.APPLICATION_JSON)
	public FacturaResponseDTO getFactura(final @QueryParam("id") Long id) {
		FacturaResponseDTO response = new FacturaResponseDTO();
		try {
			response.setFactura(getFacturaFullDTO(id));
			response.setStatus(STATUS_OK);
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(STATUS_DATABASE_EXCEPTION);
		} finally {
			return response;
		}
	}

	private FacturaFullDTO getFacturaFullDTO(Long id) {
		FacturaFullDTO dto = new FacturaFullDTO();
		FacturaAdm fa = facturaAdmService.find(id);
		Factura f = fa.getFactura();

		if (f == null) {
			dto.setPendiente(true);
		} else {
			dto.setPendiente(false);
			dto.setTipoFactura(f.getTipoFactura());
			dto.setVencimiento(ParseoService.getFormattedDate(f
					.getVencimiento()));
			dto.setVersion(f.getVersion());
			dto.setId(f.getId());
		}
		dto.setCanSave(canSave(f));
		dto.setCuit(fa.getCuit());
		dto.setDescripcion(fa.getDescripcion());
		dto.setEstado(fa.getFactura() != null ? AdministradorEstados.estados
				.get(fa.getFactura().getEstado()).getNombre() : null);
		dto.setFechaContableAdm(ParseoService.getFormattedDate(fa
				.getFechaContable()));
		dto.setFechaFactura(ParseoService.getFormattedDate(fa.getFechaFactura()));
		dto.setFechaVencimientoAdm(ParseoService.getFormattedDate(fa
				.getFechaVencimiento()));
		dto.setIdFacturaAdm(fa.getId());
		dto.setAsientos(new HashSet<AsientoFullDTO>());
		Set<String> exluidas = facturaAdmService.findJerarquiasExcluidas();
		for (FacturaAsientoAdm asiento : fa.getAsientos()) {
			if(!exluidas.contains(asiento.getJerarquia())){
				AsientoFullDTO as = new AsientoFullDTO();
				as.setDescControl(asiento.getDescripcionCentroCostos());
				as.setDescCuenta(asiento.getDescripcionCuenta());
				as.setImporteNeto(asiento.getImporteNeto());
				as.setNroCentroCostos(asiento.getNroCentroCostos());
				dto.getAsientos().add(as);
			}

		}
		dto.setImporteNeto(fa.getGralBruto());
		dto.setTotal(fa.getGralNeto());
		dto.setIva(fa.getGralIvaInc());
		dto.setNroFactura(fa.getNroFactura());
		dto.setRazonSocial(fa.getRazonSocial());
		dto.setObservacion(f != null ? f.getObservacion() : null);
		dto.setNroKanav(f != null ? f.getNroKanav() : null);
		return dto;
	}

	private void updateFatura(Factura fac, FacturaFullDTO dto) {
		fac.setTipoFactura(dto.getTipoFactura());
		fac.setVencimiento(ParseoService.parseDate(dto.getVencimiento()));
		fac.setObservacion(dto.getObservacion());
		fac.setNroKanav(dto.getNroKanav());
	}

	private Factura createFatura(FacturaFullDTO dto) {
		Factura fac = new Factura();
		fac.setTipoFactura(dto.getTipoFactura());
		fac.setVencimiento(ParseoService.parseDate(dto.getVencimiento()));
		fac.setEstado(Inicial.STATE_ID);
		fac.setFacturaAdm(dto.getIdFacturaAdm());
		fac.setObservacion(dto.getObservacion());
		fac.setNroKanav(dto.getNroKanav());
		return fac;

	}

	@SuppressWarnings("finally")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/saveFactura")
	@Consumes(MediaType.APPLICATION_JSON)
	public FacturaResponseDTO saveFactura(FacturaFullDTO factura) {
		FacturaResponseDTO response = new FacturaResponseDTO();
		response.setMessage("");
		try {
			Factura facturaSave = null;

			if (factura.getId() != null) {
				facturaSave = facturaService.find(factura.getId());
				if (!canSave(facturaSave)) {
					throw new Exception(
							"No puede modificar la factura en este estado");
				}
				updateFatura(facturaSave, factura);
			} else {
				facturaSave = createFatura(factura);
			}
			facturaSave = facturaService.saveFactura(facturaSave);
			FacturaFullDTO facturaResponse = new FacturaFullDTO();
			facturaResponse.setId(facturaSave.getId());
			response.setFactura(facturaResponse);
			response.setStatus(STATUS_OK);
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

	private boolean canSave(Factura factura) {
		return factura == null || factura.getEstado() != null
				|| factura.getEstado().equals(Estados.INICIAL);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/facturaConsultPaginatedList")
	@Consumes(MediaType.APPLICATION_JSON)
	public Page<FacturaView> getPaginatedList(
			final @QueryParam("numero") String numero,
			final @QueryParam("tipo") String tipo,
			final @QueryParam("cuit") String cuit,
			final @QueryParam("razon") String razon,
			final @QueryParam("cc") String cc,
			final @QueryParam("centroc") String centroc,
			final @QueryParam("wf") String wf,
			final @QueryParam("fvDesde") String fvDesde,
			final @QueryParam("fiDesde") String fiDesde,
			final @QueryParam("flfDesde") String flfDesde,
			final @QueryParam("fvHasta") String fvHasta,
			final @QueryParam("fiHasta") String fiHasta,
			final @QueryParam("flfHasta") String flfHasta,
			final @QueryParam("ffacDesde") String ffacDesde,
			final @QueryParam("ffacHasta") String ffacHasta,
			final @QueryParam("estado") String estado,
			final @QueryParam("page") int pageIndex,
			final @QueryParam("limit") int rowsPerPage,
			@HeaderParam(value = "vates-session-id") String sessionId) {
		UserDTO userDto = (UserDTO) this.cacheManager.getCache("defaultCache")
				.get(sessionId).get();
		return facturaAdmService.findPaginatedFilteredList(numero, tipo, cuit,
				razon, cc, centroc, wf, fvDesde, fiDesde, flfDesde, fvHasta,
				fiHasta, flfHasta, ffacDesde, ffacHasta, estado, pageIndex - 1,
				rowsPerPage, userDto.isGrantAll(), userDto.getId());

	}

}
