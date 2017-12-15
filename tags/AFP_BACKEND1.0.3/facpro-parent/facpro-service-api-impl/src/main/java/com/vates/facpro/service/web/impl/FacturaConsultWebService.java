package com.vates.facpro.service.web.impl;

import java.util.HashSet;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

import com.vates.facpro.business.states.AdministradorEstados;
import com.vates.facpro.business.states.Estados;
import com.vates.facpro.persistence.domain.Factura;
import com.vates.facpro.persistence.domain.FacturaAdm;
import com.vates.facpro.persistence.domain.FacturaAsientoAdm;
import com.vates.facpro.persistence.domain.FacturaView;
import com.vates.facpro.persistence.paging.Page;
import com.vates.facpro.persistence.service.FacturaAdmService;
import com.vates.facpro.persistence.util.ParseoService;
import com.vates.facpro.service.web.dto.AsientoFullDTO;
import com.vates.facpro.service.web.dto.FacturaFullDTO;
import com.vates.facpro.service.web.dto.FacturaResponseDTO;

@Service("facpro.service.facturaConsult")
@Path("/facturaConsult")
public class FacturaConsultWebService {

	private static final int STATUS_OK = 0;
	private static final int STATUS_DATABASE_EXCEPTION = 2;

	@Inject
	private FacturaAdmService facturaAdmService;

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
				fiHasta, flfHasta,ffacDesde ,ffacHasta, estado, pageIndex - 1, rowsPerPage);

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/facturaViewPaginatedList")
	@Consumes(MediaType.APPLICATION_JSON)
	public Page<FacturaView> getPaginatedList(
			final @QueryParam("userId") Long userId,
			final @QueryParam("page") int pageIndex,
			final @QueryParam("limit") int rowsPerPage,
			final @QueryParam("variable") String variable,
			final @QueryParam("order") Boolean order) {

		return facturaAdmService.findPaginatedFilteredList(userId,
				pageIndex - 1, rowsPerPage, variable, order);

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
			dto.setVencimiento(f.getVencimiento());
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
		dto.setFechaVencimientoAdm(ParseoService.getFormattedDate(fa
				.getFechaVencimiento()));
		dto.setIdFacturaAdm(fa.getId());
		dto.setAsientos(new HashSet<AsientoFullDTO>());
		for (FacturaAsientoAdm asiento : fa.getAsientos()) {
			AsientoFullDTO as = new AsientoFullDTO();
			as.setDescControl(asiento.getDescripcionCentroCostos());
			as.setDescCuenta(asiento.getDescripcionCuenta());
			as.setImporteNeto(asiento.getImporteNeto());
			dto.getAsientos().add(as);
		}
		dto.setImporteNeto(fa.getGralBruto());
		dto.setTotal(fa.getGralNeto());
		dto.setIva(fa.getGralIvaInc());
		dto.setNroFactura(fa.getNroFactura());
		dto.setRazonSocial(fa.getRazonSocial());
		return dto;
	}

	/*
	 * private void updateFatura(Factura fac, FacturaFullDTO dto) {
	 * fac.setTipoFactura(dto.getTipoFactura());
	 * fac.setVencimiento(dto.getVencimiento()); }
	 * 
	 * private Factura createFatura(FacturaFullDTO dto) { Factura fac = new
	 * Factura(); fac.setTipoFactura(dto.getTipoFactura());
	 * fac.setVencimiento(dto.getVencimiento());
	 * fac.setEstado(Inicial.STATE_ID);
	 * fac.setFacturaAdm(facturaAdmService.find(dto.getIdFacturaAdm())); return
	 * fac;
	 * 
	 * }
	 * 
	 * @SuppressWarnings("finally")
	 * 
	 * @POST
	 * 
	 * @Produces(MediaType.APPLICATION_JSON)
	 * 
	 * @Path("/saveFactura")
	 * 
	 * @Consumes(MediaType.APPLICATION_JSON) public FacturaResponseDTO
	 * saveFactura(FacturaFullDTO factura) { FacturaResponseDTO response = new
	 * FacturaResponseDTO(); response.setMessage(""); try { Factura facturaSave
	 * = null;
	 * 
	 * if (factura.getId() != null) { facturaSave =
	 * facturaService.find(factura.getId()); if (!canSave(facturaSave)) { throw
	 * new Exception( "No puede modificar la factura en este estado"); }
	 * updateFatura(facturaSave, factura); } else { facturaSave =
	 * createFatura(factura); } facturaService.saveFactura(facturaSave);
	 * response.setStatus(STATUS_OK); } catch (OptimisticLockException e) {
	 * response.setMessage(e.getMessage());
	 * response.setStatus(STATUS_OPTIMISTIC_EX); } catch (Exception ex) {
	 * response.setMessage(ex.getMessage());
	 * response.setStatus(STATUS_DATABASE_EXCEPTION); } finally { return
	 * response; } }
	 */

	private boolean canSave(Factura factura) {
		return factura == null || factura.getEstado() != null
				|| factura.getEstado().equals(Estados.INICIAL);
	}

}
