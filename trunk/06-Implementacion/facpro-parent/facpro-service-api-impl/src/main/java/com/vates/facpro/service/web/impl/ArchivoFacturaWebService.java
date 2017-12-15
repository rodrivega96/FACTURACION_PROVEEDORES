package com.vates.facpro.service.web.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
import com.vates.facpro.persistence.domain.ArchivoFactura;
import com.vates.facpro.persistence.domain.ArchivoHeader;
import com.vates.facpro.persistence.service.ArchivoFacturaService;
import com.vates.facpro.persistence.service.ArchivoHeaderService;
import com.vates.facpro.persistence.service.ArchivoService;
import com.vates.facpro.persistence.service.FacturaService;
import com.vates.facpro.persistence.util.ParseoService;
import com.vates.facpro.security.dto.UserDTO;
import com.vates.facpro.service.web.dto.ArchivoDTO;
import com.vates.facpro.service.web.dto.ArchivoHeaderDTO;
import com.vates.facpro.service.web.dto.ArchivoResponseDTO;
import com.vates.facpro.service.web.dto.GetArchivoHeaderDTO;

@Service("facpro.service.archivoFactura")
@Path("/archivoFactura")
public class ArchivoFacturaWebService {

	private static final int STATUS_OK = 0;
	private static final int STATUS_DATABASE_EXCEPTION = 2;
	private static final int STATUS_EXIST_FILE = 3;
	private static final int STATUS_EXIST_FILE_FAC = 4;
	private static final int STATUS_EXIST_FILE_FAC_CANT_DEL = 5;

	@Inject
	private ArchivoService archivoService;

	@Inject
	private ArchivoFacturaService archivoFacturaService;
	
	@Inject
	private FacturaService facturaService;

	@Inject
	private ArchivoHeaderService archivoHeaderService;

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
			@Multipart("idFactura") Long idFactura,
			@Multipart("force") Boolean force) {
		ArchivoResponseDTO response = new ArchivoResponseDTO();
		try {
			if (archivoHeaderService.isUnique(fileName, idFactura, fileType,
					force)) {
				saveFileInternal(fileName, type, idFactura, fileType,
						sessionId, file);
				response.setMessage("");
				response.setStatus(STATUS_OK);
			} else if (force) {
				saveFileInternal(fileName, type, idFactura, fileType,
						sessionId, file);
				response.setMessage("");
				response.setStatus(STATUS_OK);
			} else if (ArchivoHeader.FACTURA.equals(fileType)) {
				if(facturaService.canOverWriteFile(idFactura)){
					response.setStatus(STATUS_EXIST_FILE_FAC);
				}else{
					response.setStatus(STATUS_EXIST_FILE_FAC_CANT_DEL);
				}
			} else {
				response.setStatus(STATUS_EXIST_FILE);
			}
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(STATUS_DATABASE_EXCEPTION);
		} finally {
			return response;
		}
	}

	private void saveFileInternal(String fileName, String type, Long idFactura,
			Integer fileType, String sessionId, InputStream file) {
		ArchivoFactura af = new ArchivoFactura();
		try {
			UserDTO userDTO = (UserDTO)archivoFacturaService.getUserDto(cacheManager, sessionId);
			af = archivoFacturaService.saveFile(fileName, type, idFactura,
					fileType, userDTO.getId());
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			IOUtils.copy(file, bytes);
			archivoService.saveFile(bytes.toByteArray(), af.getArchivoHeader());
		} catch (IOException ex) {
			archivoFacturaService.delete(af);
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
			Long idHeader = archivoFacturaService
					.findArchivoHeaderIdByFacturaIdAndArchivoHeaderFileTypeAndArchivoHeaderDeleted(
							id, ArchivoHeader.FACTURA, false);
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
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getAllArchivoFactura")
	@Consumes(MediaType.APPLICATION_JSON)
	public ArchivoResponseDTO getAllArchivoFactura(
			final @QueryParam("id") Long id) {
		ArchivoResponseDTO response = new ArchivoResponseDTO();
		try {
			ArchivoHeaderDTO dto = new ArchivoHeaderDTO();
			dto.setSizeMax(archivoHeaderService.getSizeMax());
			List<GetArchivoHeaderDTO> headerList = new ArrayList<GetArchivoHeaderDTO>();
			for (ArchivoFactura archivoFactura : archivoFacturaService
					.findByFacturaId(id)) {
				if (!archivoFactura.getArchivoHeader().getDeleted()) {
					GetArchivoHeaderDTO dtoGet = new GetArchivoHeaderDTO();
					dtoGet.setName(archivoFactura.getArchivoHeader().getName());
					dtoGet.setId(archivoFactura.getArchivoHeader().getId());
					dtoGet.setTypeFile(ArchivoHeader.tiposArchivos
							.get(archivoFactura.getArchivoHeader()
									.getFileType()));
					dtoGet.setTypeFileId(archivoFactura.getArchivoHeader()
									.getFileType());
					dtoGet.setDate(ParseoService
							.getFormattedDate(archivoFactura.getArchivoHeader()
									.getCreatedAt()));
					dtoGet.setCanDelete(archivoHeaderService
							.canDelete(archivoFactura.getArchivoHeader()
									.getId()));
					headerList.add(dtoGet);
				}
			}
			dto.setListArchivoHeader(headerList);
			response.setHeaderDTO(dto);
			response.setStatus(STATUS_OK);
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(STATUS_DATABASE_EXCEPTION);
		} finally {
			return response;
		}
	}

}