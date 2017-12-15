package com.vates.facpro.service.web.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;
import javax.mail.MessagingException;
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

import com.vates.facpro.business.states.Estados;
import com.vates.facpro.business.states.EstadosNivel;
import com.vates.facpro.persistence.comparators.NivelesComparator;
import com.vates.facpro.persistence.domain.Factura;
import com.vates.facpro.persistence.domain.Nivel;
import com.vates.facpro.persistence.domain.NivelPublicado;
import com.vates.facpro.persistence.service.FacturaService;
import com.vates.facpro.persistence.service.NivelPublicadoService;
import com.vates.facpro.persistence.service.NivelService;
import com.vates.facpro.persistence.service.UserService;
import com.vates.facpro.security.dto.UserDTO;
import com.vates.facpro.service.web.dto.NivelDTO;
import com.vates.facpro.service.web.dto.NivelResponseDTO;

@Service("facpro.service.workFlow")
@Path("/workFlow")
public class NivelWebService {

	private static final int STATUS_OK = 0;
	private static final int STATUS_OPTIMISTIC_EX = 1;
	private static final int STATUS_DATABASE_EXCEPTION = 2;
	private static final int STATUS_FAIL_MAIL = 6;
	

	@Inject
	private NivelService nivelService;
	
	@Inject
	private NivelPublicadoService nivelPublicadoService;	

	@Inject
	private FacturaService facturaService;
	
	@Inject
	private UserService userService;
	
    @Inject
    private CacheManager cacheManager;
	
	
	@SuppressWarnings("finally")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getNivelByFactura")
	@Consumes(MediaType.APPLICATION_JSON)
	public NivelResponseDTO getNivelByFactura(final @QueryParam("id") Long id) {
		NivelResponseDTO response = new NivelResponseDTO();
		
		try {
			response.setNivelesDTO(findNivelByFactura(id));
			response.setPublicado(nivelPublicadoService
					.findNivelPublicadoByFacturaIdAndLast(id) != null);
			response.setStatus(STATUS_OK);

		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(STATUS_DATABASE_EXCEPTION);
			
		} finally {
			return response;
		}
	}

	private List<NivelDTO> findNivelByFactura(long id) {
		List<Nivel> niveles = nivelService.findNivelByFacturaIdAndEliminado(id, false);
		if (niveles == null) {
			return null;
		}
		
		List<NivelDTO> nivelDTO = new ArrayList<NivelDTO>();
		NivelDTO dto;
		for (Nivel nivel : niveles) {
			dto = new NivelDTO();
			dto.setId(nivel.getId());
			dto.setFacturaAdmId(nivel.getFactura().getId());
			dto.setOrden(nivel.getOrden());
			dto.setAutorizador(userService.find(nivel.getUsuarioId()));
			dto.setEstado(nivel.getEstado());
			dto.setEliminado(nivel.getEliminado());
			dto.setPublicado(nivel.getPublicado());
			dto.setPadre(nivel.getPadre());
			dto.setEstadoFactura(nivel.getFactura().getEstado());
			nivelDTO.add(dto);
		}
					
		return nivelDTO;
	}
	
	@SuppressWarnings("finally")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/reiniciarWF")
	@Consumes(MediaType.APPLICATION_JSON)
	public NivelResponseDTO reiniciarWF(final Long id,
			@HeaderParam(value = "vates-session-id") String sessionId) {
		NivelResponseDTO response = new NivelResponseDTO();
		response.setMessage("");
		try {
			Factura factura = facturaService.findById(id);	
			nivelService.findNivelByFacturaIdAndEliminado(id, false);
			for(Nivel nvl : nivelService.findNivelByFacturaIdAndEliminado(id, false)){
				if(!nvl.getEliminado()){
					nvl.setEliminado(true);
					nivelService.saveNivel(nvl);
				}
			}
			UserDTO userDTO = (UserDTO) this.cacheManager.getCache("defaultCache").get(sessionId).get();
			for (NivelPublicado np : factura.getNivelesPublicados()) {
				if (np.getLast()) {
					np.setLast(false);
					np.setUpdateBy(userDTO.getId());
					nivelPublicadoService.saveNivelPublicado(np);
				}
			}
			factura.setEstado(Estados.INICIAL);
			facturaService.saveFactura(factura);
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
	@Path("/saveNivel")
	@Consumes(MediaType.APPLICATION_JSON)
	public NivelResponseDTO saveNivel(List<NivelDTO> nivelesDTO, @HeaderParam(value = "vates-session-id") String sessionId) {
		NivelResponseDTO response = new NivelResponseDTO();
		response.setMessage("");
		try {
			Factura f = facturaService.findById(nivelesDTO.get(0).getFacturaAdmId());
			UserDTO userDTO = (UserDTO) this.cacheManager.getCache("defaultCache").get(sessionId).get();
			saveNivel(f, userDTO.getId(),nivelesDTO);
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
	
	
	
	@SuppressWarnings("finally")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/saveAndPublishNivel")
	@Consumes(MediaType.APPLICATION_JSON)
	public NivelResponseDTO saveAndPublishNivel(List<NivelDTO> niveles, @HeaderParam(value = "vates-session-id") String sessionId) {
		NivelResponseDTO response = new NivelResponseDTO();
		response.setMessage("");
		try {
			UserDTO userDTO = (UserDTO) this.cacheManager.getCache("defaultCache").get(sessionId).get();			
			Factura f = facturaService.find(niveles.get(0).getFacturaAdmId());
			f = saveNivel(f, userDTO.getId(), niveles);
			publish(f, userDTO.getId());		
			response.setStatus(STATUS_OK);
		} catch (OptimisticLockException e) {
			response.setMessage(e.getMessage());
			response.setStatus(STATUS_OPTIMISTIC_EX);
		} catch (MessagingException e) {
			response.setMessage(e.getMessage());
			response.setStatus(STATUS_FAIL_MAIL);
		} catch (Exception ex) {
			ex.printStackTrace();
			response.setMessage(ex.getMessage());
			response.setStatus(STATUS_DATABASE_EXCEPTION);
		} finally {
			return response;
		}
	}
	
	protected Factura saveNivel(Factura f, Long userId,List<NivelDTO> nivelesDTO) throws Exception{
		List<Nivel> nivelesActuales = new ArrayList<Nivel>();
		for(Nivel n1 : nivelService.findNivelByFacturaIdAndEliminado(
				f.getId(), false)){
			if (!n1.getEliminado()) {
				nivelesActuales.add(n1);
			}
		}
		f.setWfCreatedUser(nivelesActuales == null || nivelesActuales.isEmpty() ? userId
				: f.getWfCreatedUser());
		f.setWfUpdatedUser(userId);
		for (Nivel nivel : nivelesActuales) {
			if (nivel.getEstado() != EstadosNivel.AUTORIZADO
					&& nivel.getEstado() != EstadosNivel.OBSERVADO
					&& nivel.getEstado() != EstadosNivel.RECHAZADO) {
				if (nivel.getPublicado()) {
					nivel.setEliminado(true);
					nivelService.saveNivel(nivel);
				} else {
					nivelService.delete(nivel);
				}
			}
		}	
		f = facturaService.saveAndFlushFactura(f);
		List<Nivel> nivelesList = new ArrayList<Nivel>();
		for (NivelDTO dto : nivelesDTO) {				
			Nivel n = null;
			if(dto.getPublicado() && dto.getId() != null){
				n = nivelService.find(dto.getId());
				n.setEliminado(true);
				Nivel nuevo = new Nivel();		//clonar
					nuevo.setPadre(dto.getId());
					nuevo.setFactura(f);
					nuevo.setOrden(dto.getOrden());
					nuevo.setUsuarioId(dto.getAutorizador().getId());
					nuevo.setEstado(dto.getEstado());
					nuevo.setEliminado(false);
					nuevo.setPublicado(dto.getPublicado());
				nivelesList.add(nuevo);
			}else{
				n = new Nivel();
				n.setFactura(f);
				n.setOrden(dto.getOrden());
				n.setUsuarioId(dto.getAutorizador().getId());
				n.setEstado(EstadosNivel.EN_AUTORIZACION);
				n.setEliminado(false);
				n.setPublicado(false);
			}
			nivelesList.add(n);
		}				
		nivelService.saveNiveles(nivelesList);
		return f;
	}
	
	
	protected Factura publish(Factura f, Long userId) throws Exception,
			MessagingException {
		List<Nivel> nivelesActuales = new ArrayList<Nivel>();
		for (Nivel n1 : nivelService.findNivelByFacturaIdAndEliminado(
				f.getId(), false)) {
			if (!n1.getEliminado()) {
				n1.setPublicado(true);
				nivelesActuales.add(n1);
			}
		}
		nivelService.saveNiveles(nivelesActuales);
		NivelPublicado nivelPublicado = nivelPublicadoService
				.findNivelPublicadoByFacturaIdAndLast(f.getId());
		if (nivelPublicado != null) {
			nivelPublicado.setLast(false);		
		}else{
			nivelPublicado= new NivelPublicado();
			nivelPublicado.setFactura(f);
			nivelPublicado.setNiveles(new HashSet<Nivel>(nivelesActuales));
			nivelPublicado.setLast(true);
			nivelPublicado.setCreatedUser(userId);
			nivelPublicado.setUpdateBy(userId);	
		}
		nivelPublicadoService.saveNivelPublicado(nivelPublicado);
		Collections.sort(nivelesActuales, new NivelesComparator());
		for (Nivel n : nivelesActuales) {
			if (n.getEstado() == EstadosNivel.EN_AUTORIZACION) {
				f.setNivel(n);
				f.setEstado(Estados.EN_AUTORIZACION);
				f = facturaService.saveFactura(f);
				nivelService.informarPendienteAutorizar(n);
				return f;
			}
		}
		return f;
	}

}
