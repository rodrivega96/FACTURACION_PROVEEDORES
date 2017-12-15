package com.vates.facpro.service.web.impl;

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

import com.vates.facpro.persistence.domain.ProveedorView;
import com.vates.facpro.persistence.paging.Page;
import com.vates.facpro.persistence.service.ProveedorService;
import com.vates.facpro.security.dto.UserDTO;
import com.vates.facpro.service.web.dto.DatoInicialProveedorDTO;
import com.vates.facpro.service.web.dto.ProveedorResponseDTO;

@Service("facpro.service.proveedor")
@Path("/proveedor")
public class ProveedorWebService {

	private static final int STATUS_OK = 0;
	private static final int STATUS_OPTIMISTIC_EX = 1;
	private static final int STATUS_DATABASE_EXCEPTION = 2;

	@Inject
	private ProveedorService proveedorService;
	
	@Inject
	private CacheManager cacheManager;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/proveedorPaginatedList")
	@Consumes(MediaType.APPLICATION_JSON)
	public Page<ProveedorView> getPaginatedList(
			final @QueryParam("razonSocial") String razonSocial,
			final @QueryParam("cuit") String cuit,
			final @QueryParam("medioPago") String medioPago,
			final @QueryParam("page") int pageIndex,
			final @QueryParam("limit") int rowsPerPage) {

		return proveedorService.find(razonSocial, cuit, medioPago, pageIndex,
				rowsPerPage);

	}


	@SuppressWarnings("finally")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getProveedor")
	@Consumes(MediaType.APPLICATION_JSON)
	public ProveedorResponseDTO getProveedor(final @QueryParam("id") Long id) {
		ProveedorResponseDTO response = new ProveedorResponseDTO();
		try {
			ProveedorView pro = proveedorService.findViewById(id);
			if(pro==null){
				throw new OptimisticLockException();
			}
			response.setProveedor(pro);
			response.setStatus(STATUS_OK);
		} catch (OptimisticLockException ex1){
			response.setMessage("");
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
	@Path("/saveProveedor")
	@Consumes(MediaType.APPLICATION_JSON)
	public ProveedorResponseDTO saveProveedor(ProveedorView proveedorView, @HeaderParam(value = "vates-session-id") String sessionId) {
		ProveedorResponseDTO response = new ProveedorResponseDTO();
		response.setMessage("");
		try {
			UserDTO userDTO = (UserDTO)proveedorService.getUserDto(cacheManager, sessionId);
			proveedorService.saveProveedorView(proveedorView, userDTO.getId());
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
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getDatoInicial")
	@Consumes(MediaType.APPLICATION_JSON)
	public ProveedorResponseDTO getDatoInicial() {
		ProveedorResponseDTO response = new ProveedorResponseDTO();
		try {
			DatoInicialProveedorDTO datoDTO = getDatosIniciales();
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
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public ProveedorResponseDTO delete(Long id) {
		ProveedorResponseDTO response = new ProveedorResponseDTO();
		try {
			proveedorService.deleteProveedor(id);
			response.setStatus(STATUS_OK);
			response.setMessage("");
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(STATUS_DATABASE_EXCEPTION);
		} finally {
			return response;
		}
	}
	
	protected DatoInicialProveedorDTO getDatosIniciales() {
		DatoInicialProveedorDTO dto = new DatoInicialProveedorDTO();
		dto.setMediosPago(proveedorService.getMediosPago());
		dto.setTiposTelefono(proveedorService.getTiposTelefono());
		return dto;
	}
	

}

