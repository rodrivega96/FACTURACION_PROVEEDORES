package com.vates.facpro.service.web.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.mail.MessagingException;
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

import com.vates.facpro.business.states.factura.Estados;
import com.vates.facpro.persistence.domain.Factura;
import com.vates.facpro.persistence.domain.FacturaAdm;
import com.vates.facpro.persistence.domain.FacturaAsientoAdm;
import com.vates.facpro.persistence.domain.HistorialAutorizacion;
import com.vates.facpro.persistence.domain.Kanav;
import com.vates.facpro.persistence.domain.Nivel;
import com.vates.facpro.persistence.domain.User;
import com.vates.facpro.persistence.service.FacturaAdmService;
import com.vates.facpro.persistence.service.HistorialAutorizacionService;
import com.vates.facpro.persistence.service.KanavService;
import com.vates.facpro.persistence.service.NivelService;
import com.vates.facpro.persistence.service.UserService;
import com.vates.facpro.persistence.util.ParseoService;
import com.vates.facpro.security.dto.UserDTO;
import com.vates.facpro.service.web.comparators.HistorialComparatorDate;
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
	
	@Inject
	private UserService userService;
	
	@Inject
	private NivelService nivelService;
	
	@Inject
	private KanavService kanavService;
	
	@Inject
    private CacheManager cacheManager;

	@SuppressWarnings("finally")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getHistorialByFinalUser")
	@Consumes(MediaType.APPLICATION_JSON)
	public HistorialAutorizacionResponseDTO getHistorialByFinalUser(
			final @QueryParam("idFactura") Long idFactura) {
		HistorialAutorizacionResponseDTO response = new HistorialAutorizacionResponseDTO();
		try {
			response.setDto(getHistorialFacturaDTO(idFactura));
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
			AutorizacionAnteriorDTO dto, @HeaderParam(value = "vates-session-id") String sessionId) {
		HistorialAutorizacionResponseDTO response = new HistorialAutorizacionResponseDTO();
		try {
			UserDTO userDTO = (UserDTO) this.cacheManager.getCache("defaultCache").get(sessionId).get();
			historialService.autorizarAnterioresHistorial(dto.getId(),
					userDTO.getId(), dto.getDescripcion(), dto.getEstado());
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
			RechazarAnteriorDTO dto, @HeaderParam(value = "vates-session-id") String sessionId) {
		HistorialAutorizacionResponseDTO response = new HistorialAutorizacionResponseDTO();
		try {
			UserDTO userDTO = (UserDTO) this.cacheManager.getCache("defaultCache").get(sessionId).get();
			historialService.rechazarAnterioresHistorial(dto.getId(),
					userDTO.getId(), dto.getDescripcion());
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

	private HistorialAutorizacionDTO getHistorialFacturaDTO(Long idFactura) {
		HistorialAutorizacionDTO dto = new HistorialAutorizacionDTO();
		FacturaAdm fa = facturaAdmService.find(idFactura);
		Factura f = fa.getFactura();
		
		dto.setCuit(fa.getCuit());
		dto.setDescripcion(fa.getDescripcion());
		dto.setAsientos(new HashSet<AsientoFullDTO>());
		
		if (f != null && f.getNroKanav() != null) {
			dto.setKanav(f.getNroKanav());
			try {
				Kanav kanav = kanavService.findById(f.getNroKanav());
				if (kanav != null) {
					dto.setKanavState(kanav.getState());
				} else {
					dto.setKanavState("No se encontro la solicitud de Kanav");
				}
			} catch (Exception e) {
				dto.setKanavState("No se puede acceder a la base de datos de Kanav");
			}
		}
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
		dto.setVencimiento(ParseoService.getFormattedDate(f.getVencimiento()));
		dto.setFechaFactura(ParseoService.getFormattedDate(fa.getFechaFactura()));
		dto.setHistorials(new ArrayList<HistorialWFDTO>());
		for (HistorialAutorizacion his : historialService.findByuserAndFactura(f.getId())) {
			User usuario = userService.find(his.getNivel().getUsuarioId());
			HistorialWFDTO newDTO = new HistorialWFDTO();
			newDTO.setNivel(his.getNivel().getOrden());
			newDTO.setAutoriza(usuario.getLastName() + ", "
					+ usuario.getName());
			newDTO.setDescripcion(his.getDescripcion());
			newDTO.setEstado(his.getEstado());
			newDTO.setFecha(ParseoService.getFormattedTime(his.getUpdatedAt()));
			newDTO.setUpdate(his.getUpdatedAt());
			dto.getHistorials().add(newDTO);
		}
		if (fa.getFactura().getEstado().equals(Estados.EN_AUTORIZACION)) {
			for (Nivel n : nivelService.findNivelByFacturaId(f.getId())) {
				if (!n.getEliminado()
						&& fa.getFactura().getNivel().getOrden() <= n
								.getOrden()) {
					User usuario = userService.find(n.getUsuarioId());
					HistorialWFDTO newDTO = new HistorialWFDTO();
					newDTO.setNivel(n.getOrden());
					newDTO.setAutoriza(usuario.getLastName() + ", "
							+ usuario.getName());
					newDTO.setDescripcion("-");
					newDTO.setEstado(HistorialAutorizacion.PENDIENTE);
					newDTO.setFecha("-");
					newDTO.setUpdate(null);
					dto.getHistorials().add(newDTO);
				}
			}
		}
		Collections.sort(dto.getHistorials(), new HistorialComparatorDate());
		return dto;
	}
	
	@SuppressWarnings("finally")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/informarFacturaHistorial")
	@Consumes(MediaType.APPLICATION_JSON)
	public HistorialAutorizacionResponseDTO informarFacturaHistorial(
			final @QueryParam("id") Long idFactura,
			@HeaderParam(value = "vates-session-id") String sessionId) {
		HistorialAutorizacionResponseDTO response = new HistorialAutorizacionResponseDTO();
		try {
			UserDTO userDTO = (UserDTO) this.cacheManager
					.getCache("defaultCache").get(sessionId).get();
			historialService.informar(idFactura, userDTO.getId());
			response.setStatus(STATUS_OK);
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(STATUS_DATABASE_EXCEPTION);
		} finally {
			return response;
		}
	}

}
