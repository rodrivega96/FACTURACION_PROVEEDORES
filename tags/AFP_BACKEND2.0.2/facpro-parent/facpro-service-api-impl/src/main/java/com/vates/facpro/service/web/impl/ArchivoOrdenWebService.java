package com.vates.facpro.service.web.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.vates.facpro.persistence.domain.Archivo;
import com.vates.facpro.persistence.domain.ArchivoHeader;
import com.vates.facpro.persistence.domain.ArchivoOrden;
import com.vates.facpro.persistence.service.ArchivoHeaderService;
import com.vates.facpro.persistence.service.ArchivoOrdenService;
import com.vates.facpro.persistence.service.ArchivoService;
import com.vates.facpro.persistence.util.ParseoService;
import com.vates.facpro.security.dto.UserDTO;
import com.vates.facpro.service.web.dto.ArchivoDTO;
import com.vates.facpro.service.web.dto.ArchivoOrdenDTO;
import com.vates.facpro.service.web.dto.ArchivoResponseDTO;

@Service("facpro.service.archivoOrden")
@Path("/archivoOrden")
public class ArchivoOrdenWebService {

	private static final int STATUS_OK = 0;
	private static final int STATUS_DATABASE_EXCEPTION = 2;
	private static final int STATUS_EXIST_FILE = 3;

	@Inject
	private ArchivoService archivoService;

	@Inject
	private ArchivoHeaderService archivoHeaderService;
	
	@Inject
	private ArchivoOrdenService archivoOrdenService;

	@Inject
	private CacheManager cacheManager;

	@SuppressWarnings("finally")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/uploadFile")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public ArchivoResponseDTO uploadFile(
			@HeaderParam(value = "vates-session-id") String sessionId,
			@Multipart("file") InputStream file,
			@Multipart("fileName") String fileName,
			@Multipart("type") String type,
			@Multipart("fileType") Integer fileType,
			@Multipart("idFactura") Long idOrden,
			@Multipart("force") Boolean update, @Multipart("idTmp") String idTmp) {
		ArchivoResponseDTO response = new ArchivoResponseDTO();
		try {
			ArchivoHeader ah;
			if (!update) {
				if(archivoHeaderService.isUniqueFileTmp(fileName, fileType,
						idTmp)){
					ah = saveFileInternal(fileName, type, idTmp, fileType,
							sessionId, file);
					response.setArchivoOrdenDTO(getArchivoOrdenDTO(ah));
					response.setStatus(STATUS_OK);					
				} else {
					response.setStatus(STATUS_EXIST_FILE);
				}
			} else {
				if (archivoHeaderService.isUniqueFileOrden(fileName, fileType,
						idOrden)) {
					ah = saveFile(fileName, type, idOrden, fileType, sessionId,
							file);
					response.setArchivoOrdenDTO(getArchivoOrdenDTO(ah));
					response.setStatus(STATUS_OK);
				} else {
					response.setStatus(STATUS_EXIST_FILE);
				}
			}
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
	@Path("/verArchivo")
	@Consumes(MediaType.APPLICATION_JSON)
	public ArchivoResponseDTO verArchivo(final @QueryParam("id") Long id) {
		ArchivoResponseDTO response = new ArchivoResponseDTO();
		try {
			Long idHeader = archivoOrdenService
					.findArchivoHeaderIdByOrdenIdAndArchivoHeaderFileTypeAndArchivoHeaderDeleted(
							id, ArchivoHeader.OC, false);
			if (idHeader != null) {
				ArchivoDTO dto = new ArchivoDTO();
				Archivo archivo = archivoService.findByHeaderId(idHeader);
				dto.setId(archivo.getId());
				dto.setContent(archivo.getContenidoBlob());
				response.setDto(dto);
			}
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
	@Path("/deleteFile")
	@Consumes(MediaType.APPLICATION_JSON)
	public ArchivoResponseDTO deleteFile(ArchivoOrdenDTO dto) {
		ArchivoResponseDTO response = new ArchivoResponseDTO();
		try {
			archivoHeaderService.deleteByHeader(
					archivoHeaderService.find(dto.getId()), false);
			response.setMessage("");
			response.setStatus(STATUS_OK);
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(STATUS_DATABASE_EXCEPTION);
		} finally {
			return response;
		}
	}

	private ArchivoHeader saveFile(String fileName, String type, Long idOrden,
			Integer fileType, String sessionId, InputStream file)
			throws IOException {
		UserDTO userDTO = (UserDTO) this.cacheManager.getCache("defaultCache")
				.get(sessionId).get();
		ArchivoOrden ao = archivoOrdenService.updateFile(fileName, type,
				idOrden, fileType, userDTO.getId());
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		IOUtils.copy(file, bytes);
		archivoService.saveFile(bytes.toByteArray(), ao.getArchivoHeader());
		return ao.getArchivoHeader();
	}

	private ArchivoHeader saveFileInternal(String fileName, String type,
			String idTmp, Integer fileType, String sessionId, InputStream file)
			throws IOException {
		ArchivoHeader ah = new ArchivoHeader();
		UserDTO userDTO = (UserDTO) this.cacheManager.getCache("defaultCache")
				.get(sessionId).get();
		ah = archivoHeaderService.saveFile(fileName, type, idTmp, fileType,
				userDTO.getId());
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		IOUtils.copy(file, bytes);
		archivoService.saveFile(bytes.toByteArray(), ah);
		return ah;
	}

	private ArchivoOrdenDTO getArchivoOrdenDTO(ArchivoHeader ah) {
		ArchivoOrdenDTO dto = new ArchivoOrdenDTO();
		dto.setDate(ah.getCreatedAt() != null ? ParseoService
				.getFormattedDate(ah.getCreatedAt()) : null);
		dto.setId(ah.getId());
		dto.setName(ah.getName());
		dto.setType(ah.getFileType());
		return dto;
	}

}