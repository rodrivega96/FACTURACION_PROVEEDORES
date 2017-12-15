package com.vates.facpro.service.web.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

import com.vates.facpro.business.states.Estados;
import com.vates.facpro.persistence.comparators.NivelesComparator;
import com.vates.facpro.persistence.domain.Factura;
import com.vates.facpro.persistence.domain.FacturaAdm;
import com.vates.facpro.persistence.domain.FacturaAsientoAdm;
import com.vates.facpro.persistence.domain.HistorialAutorizacion;
import com.vates.facpro.persistence.domain.Nivel;
import com.vates.facpro.persistence.service.FacturaAdmService;
import com.vates.facpro.persistence.service.HistorialAutorizacionService;
import com.vates.facpro.persistence.util.ParseoService;
import com.vates.facpro.service.web.dto.AsientoFullDTO;
import com.vates.facpro.service.web.dto.AutorizacionAnteriorDTO;
import com.vates.facpro.service.web.dto.AutorizacionDTO;
import com.vates.facpro.service.web.dto.HistorialAutorizacionDTO;
import com.vates.facpro.service.web.dto.HistorialAutorizacionResponseDTO;
import com.vates.facpro.service.web.dto.HistorialWFDTO;
import com.vates.facpro.service.web.dto.RechazarAnteriorDTO;

@Service("facpro.service.historialAutorizacion")
@Path("/historial")
public class HistorialAutorizacionWebService {

	private static final int STATUS_OK = 0;
	private static final int STATUS_DATABASE_EXCEPTION = 2;
	private static final int STATUS_MAIL_EXCEPTION = 3;

	@Inject
	private FacturaAdmService facturaAdmService;

	@Inject
	private HistorialAutorizacionService historialService;

	@SuppressWarnings("finally")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getHistorialByFinalUser")
	@Consumes(MediaType.APPLICATION_JSON)
	public HistorialAutorizacionResponseDTO getHistorialByFinalUser(
			final @QueryParam("idFactura") Long idFactura,
			final @QueryParam("idUsuario") Long idUsuario) {
		HistorialAutorizacionResponseDTO response = new HistorialAutorizacionResponseDTO();
		try {
			response.setDto(getHistorialFacturaDTO(idFactura, idUsuario));
			response.setStatus(STATUS_OK);
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
	@Path("/saveHistorial")
	@Consumes(MediaType.APPLICATION_JSON)
	public HistorialAutorizacionResponseDTO saveHistorial(AutorizacionDTO dto) {
		HistorialAutorizacionResponseDTO response = new HistorialAutorizacionResponseDTO();
		try {
			historialService.saveHistorial(dto.getId(), dto.getDescripcion(),
					dto.getEstado());
			response.setStatus(STATUS_OK);
			response.setMessage("");
		} catch (MessagingException ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(STATUS_MAIL_EXCEPTION);
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
	@Path("/rechazarFacturaHistorial")
	@Consumes(MediaType.APPLICATION_JSON)
	public HistorialAutorizacionResponseDTO rechazarFacturaHistorial(
			AutorizacionDTO dto) {
		HistorialAutorizacionResponseDTO response = new HistorialAutorizacionResponseDTO();
		try {
			historialService.rechazarFacturaHistorial(dto.getId(),
					dto.getDescripcion());
			response.setStatus(STATUS_OK);
			response.setMessage("");
		} catch (MessagingException ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(STATUS_MAIL_EXCEPTION);
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
	@Path("/saveAnterioresHistorial")
	@Consumes(MediaType.APPLICATION_JSON)
	public HistorialAutorizacionResponseDTO autorizarAnterioresHistorial(
			AutorizacionAnteriorDTO dto) {
		HistorialAutorizacionResponseDTO response = new HistorialAutorizacionResponseDTO();
		try {
			historialService.autorizarAnterioresHistorial(dto.getId(),
					dto.getUserId(), dto.getDescripcion(), dto.getEstado());
			response.setStatus(STATUS_OK);
			response.setMessage("");
		} catch (MessagingException ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(STATUS_MAIL_EXCEPTION);
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
	@Path("/rechazarAnterioresHistorial")
	@Consumes(MediaType.APPLICATION_JSON)
	public HistorialAutorizacionResponseDTO rechazarAnterioresHistorial(
			RechazarAnteriorDTO dto) {
		HistorialAutorizacionResponseDTO response = new HistorialAutorizacionResponseDTO();
		try {
			historialService.rechazarAnterioresHistorial(dto.getId(),
					dto.getUserId(), dto.getDescripcion());
			response.setStatus(STATUS_OK);
			response.setMessage("");
		} catch (MessagingException ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(STATUS_MAIL_EXCEPTION);
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(STATUS_DATABASE_EXCEPTION);
		} finally {
			return response;
		}
	}

	private HistorialAutorizacionDTO getHistorialFacturaDTO(Long idFactura,
			Long idUsuario) {
		HistorialAutorizacionDTO dto = new HistorialAutorizacionDTO();
		FacturaAdm fa = facturaAdmService.find(idFactura);
		Factura f = fa.getFactura();
		dto.setCuit(fa.getCuit());
		dto.setDescripcion(fa.getDescripcion());
		Double importeTotal = 0D;		
		dto.setAsientos(new HashSet<AsientoFullDTO>());
		for (FacturaAsientoAdm asiento : fa.getAsientos()) {
			AsientoFullDTO as = new AsientoFullDTO();
			as.setDescControl(asiento.getDescripcionCentroCostos());
			as.setDescCuenta(asiento.getDescripcionCuenta());
			as.setImporteNeto(asiento.getImporteNeto());
			as.setNroCentroCostos(asiento.getNroCentroCostos());
			dto.getAsientos().add(as);
			importeTotal += asiento.getImporteNeto();
		}
		
		dto.setImporteNeto(importeTotal);
		dto.setNroFactura(fa.getNroFactura());
		dto.setRazonSocial(fa.getRazonSocial());
		dto.setVencimiento(ParseoService.getFormattedDate(f.getVencimiento()));
		dto.setFechaFactura(ParseoService.getFormattedDate(fa.getFechaFactura()));
		dto.setHistorials(new ArrayList<HistorialWFDTO>());
		for (HistorialAutorizacion his : historialService.findByuserAndFactura(
				idUsuario, f.getId())) {
			HistorialWFDTO newDTO = new HistorialWFDTO();
			newDTO.setNivel(his.getNivel().getOrden());
			newDTO.setAutoriza(his.getNivel().getUsuario().getLastName() + ", "
					+ his.getNivel().getUsuario().getName());
			newDTO.setDescripcion(his.getDescripcion());
			newDTO.setEstado(his.getEstado());
			newDTO.setFecha(ParseoService.getFormattedTime(his.getUpdatedAt()));
			dto.getHistorials().add(newDTO);
		}
		if (fa.getFactura().getEstado().equals(Estados.EN_AUTORIZACION)) {
			HistorialWFDTO newDTO = new HistorialWFDTO();
			newDTO.setNivel(dto.getHistorials().size() + 1);
			newDTO.setAutoriza(fa.getFactura().getNivel().getUsuario()
					.getLastName()
					+ ", " + fa.getFactura().getNivel().getUsuario().getName());
			newDTO.setDescripcion("-");
			newDTO.setEstado(HistorialAutorizacion.PENDIENTE);
			newDTO.setFecha("-");
			dto.getHistorials().add(newDTO);
		}
		
		if (dto.getHistorials().size() < f.getNiveles().size()) {
			List<Nivel> nivelesAux = new ArrayList<Nivel>(f.getNiveles());
			Collections.sort(nivelesAux, new NivelesComparator());
			
			for(Nivel n : nivelesAux) {				
				if (n.getOrden() > dto.getHistorials().size()) {
					HistorialWFDTO newDTO = new HistorialWFDTO();
					newDTO.setNivel(n.getOrden());
					newDTO.setAutoriza(n.getUsuario().getLastName()
							+ ", " + n.getUsuario().getName());
					newDTO.setDescripcion("-");
					newDTO.setEstado(HistorialAutorizacion.PENDIENTE);
					newDTO.setFecha("-");
					dto.getHistorials().add(newDTO);
				}
			}
		}
		
		return dto;
	}

}
