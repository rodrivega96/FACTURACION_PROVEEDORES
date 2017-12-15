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
		for(Nivel nivel : niveles){
			dto = new NivelDTO();
			dto.setId(nivel.getId());
			dto.setFacturaAdmId(nivel.getFactura().getId());
			dto.setOrden(nivel.getOrden());
			dto.setAutorizador(nivel.getUsuario());
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
	@Path("/saveNivel")
	@Consumes(MediaType.APPLICATION_JSON)
	public NivelResponseDTO saveNivel(List<NivelDTO> nivelesDTO, @HeaderParam(value = "vates-session-id") String sessionId) {
		NivelResponseDTO response = new NivelResponseDTO();
		response.setMessage("");
		try {
			Factura f = facturaService.findById(nivelesDTO.get(0).getFacturaAdmId());
			UserDTO userDTO = (UserDTO) this.cacheManager.getCache("defaultCache").get(sessionId).get();
			
			List<Nivel> nivelesActuales = new ArrayList<Nivel>();
			for(Nivel n1 : f.getNiveles()){
				if (n1.getEliminado() == false) {
					nivelesActuales.add(n1);
				}
			}
			
			if(nivelesActuales == null || nivelesActuales.isEmpty()) {
				f.setWfCreatedUser(userDTO.getId());
			}			
			f.setWfUpdatedUser(userDTO.getId());
			facturaService.saveAndFlushFactura(f);
			
			for (Nivel nivel : nivelesActuales) {
				if (nivel.getEstado() == EstadosNivel.AUTORIZADO 
						|| nivel.getEstado() == EstadosNivel.OBSERVADO 
						|| nivel.getEstado() == EstadosNivel.RECHAZADO) { 
					continue;
				}
				if (nivel.getPublicado()) {
					nivel.setEliminado(true);
					nivelService.saveNivel(nivel);
					continue;
				}
				nivelService.delete(nivel);				
			}			
					
			
			List<Nivel> nivelesList = new ArrayList<Nivel>();
			
			for (NivelDTO dto : nivelesDTO) {				
				Nivel n = null;
				
				if (dto.getId() != null) { //existente
					n = nivelService.find(dto.getId());
					if(dto.getPublicado()) { //publicado
						n.setEliminado(true);
						//if (dto.getEstado() != EstadosNivel.RECHAZADO) {					
							Nivel nuevo = new Nivel();		//clonar
								nuevo.setPadre(dto.getId());
								nuevo.setFactura(f);
								nuevo.setOrden(dto.getOrden());
								nuevo.setUsuario(dto.getAutorizador());
								nuevo.setEstado(dto.getEstado());
								nuevo.setEliminado(false);
								nuevo.setPublicado(dto.getPublicado());
							nivelesList.add(nuevo);
						//}
					}else{ // No publicado
						n = new Nivel();
							//n.setPadre(dto.getId());
							n.setFactura(f);
							n.setOrden(dto.getOrden());
							n.setUsuario(dto.getAutorizador());
							n.setEstado(EstadosNivel.EN_AUTORIZACION);
							n.setEliminado(false);
							n.setPublicado(false);
					}
				}else{ //nuevo
					n = new Nivel();		
					n.setFactura(f);
					n.setOrden(dto.getOrden());
					n.setUsuario(dto.getAutorizador());
					n.setEstado(EstadosNivel.EN_AUTORIZACION);
					n.setEliminado(false);
					n.setPublicado(false);
				}
				
				nivelesList.add(n);
			}				
			
			nivelService.saveNiveles(nivelesList);
			f.setNiveles(new HashSet<Nivel>(nivelesActuales));
			
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
		NivelResponseDTO response = saveNivel(niveles, sessionId);		
		if (response.getStatus()!=0) {
			return response;
		}			
		
		try {
			UserDTO userDTO = (UserDTO) this.cacheManager.getCache("defaultCache").get(sessionId).get();			
			Factura f = facturaService.find(niveles.get(0).getFacturaAdmId());

			List<Nivel> nivelesActuales = new ArrayList<Nivel>();
			for(Nivel n1 : f.getNiveles()){
				if (n1.getEliminado() == false) {
					n1.setPublicado(true);
					nivelesActuales.add(n1);
				}
			}	
						
			nivelService.saveNiveles(nivelesActuales);						
			
			NivelPublicado npExistente  = nivelPublicadoService.findNivelPublicadoByFacturaIdAndLast(f.getId());
			if (npExistente != null) {
				npExistente.setLast(false);
				nivelPublicadoService.saveNivelPublicado(npExistente);
			}				
				
			NivelPublicado np = new NivelPublicado();
				np.setFactura(f);
				np.setNiveles(new HashSet<Nivel>(nivelesActuales));
				np.setLast(true);
				np.setCreatedUser(userDTO.getId());
			nivelPublicadoService.saveNivelPublicado(np);	
			
			Collections.sort(nivelesActuales, new NivelesComparator());
			for(Nivel n : nivelesActuales) {
				if (n.getEstado() == EstadosNivel.EN_AUTORIZACION) {
					f.setNivel(n);
					f.setEstado(Estados.EN_AUTORIZACION);
					facturaService.saveFactura(f);				
					nivelService.informarPendienteAutorizar(n);
					break;
				}
			}
						
			response.setStatus(STATUS_OK);
			
		} catch (OptimisticLockException e) {
			response.setMessage(e.getMessage());
			response.setStatus(STATUS_OPTIMISTIC_EX);
		} catch (MessagingException e) {
			response.setMessage(e.getMessage());
			response.setStatus(STATUS_FAIL_MAIL);
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(STATUS_DATABASE_EXCEPTION);
		} finally {
			return response;
		}
	}

}
