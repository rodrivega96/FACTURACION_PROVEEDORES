package com.vates.facpro.service.web.impl;

import javax.inject.Inject;
import javax.persistence.OptimisticLockException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

import com.vates.facpro.persistence.domain.Archivo;
import com.vates.facpro.persistence.service.ArchivoHeaderService;
import com.vates.facpro.persistence.service.ArchivoService;
import com.vates.facpro.service.web.dto.ArchivoDTO;
import com.vates.facpro.service.web.dto.ArchivoDeleteDTO;
import com.vates.facpro.service.web.dto.ArchivoResponseDTO;

@Service("facpro.service.archivo")
@Path("/archivo")
public class ArchivoWebService {

	private static final int STATUS_OK = 0;
	private static final int STATUS_OPTIMISTIC_EX = 1;
	private static final int STATUS_DATABASE_EXCEPTION = 2;

	@Inject
	private ArchivoService archivoService;
	
	@Inject
	private ArchivoHeaderService archivoHeaderService;

	@SuppressWarnings("finally")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/downloadArchivo")
	@Consumes(MediaType.APPLICATION_JSON)
	public ArchivoResponseDTO downloadArchivo(final @QueryParam("id") Long idHeader) {
		ArchivoResponseDTO response = new ArchivoResponseDTO();
		try {
			Archivo archivo = archivoService.findByHeaderId(idHeader);
			ArchivoDTO dto = new ArchivoDTO();
			dto.setId(archivo.getId());
			dto.setContent(archivo.getContenidoBlob());
			response.setDto(dto);
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
	@Path("/deleteArchivo")
	@Consumes(MediaType.APPLICATION_JSON)
	public ArchivoResponseDTO deleteArchivo(ArchivoDeleteDTO dto) {
		ArchivoResponseDTO response = new ArchivoResponseDTO();
		try {
			if(!archivoHeaderService.canDelete(dto.getIdArchivoHeader())){
				throw new Exception("No tiene privilegios para eliminar el archivo");
			}
			archivoService.deleteFile(dto.getIdArchivoHeader());
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