package com.vates.facpro.service.web.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.springframework.stereotype.Service;

import com.vates.facpro.persistence.domain.ArchivoFactura;
import com.vates.facpro.persistence.service.ArchivoFacturaService;
import com.vates.facpro.persistence.service.ArchivoHeaderService;
import com.vates.facpro.persistence.service.ArchivoService;
import com.vates.facpro.persistence.util.ParseoService;
import com.vates.facpro.service.web.dto.ArchivoHeaderDTO;
import com.vates.facpro.service.web.dto.ArchivoResponseDTO;
import com.vates.facpro.service.web.dto.GetArchivoHeaderDTO;

@Service("facpro.service.archivoFactura")
@Path("/archivoFactura")
public class ArchivoFacturaWebService {

	private static final int STATUS_OK = 0;
	private static final int STATUS_DATABASE_EXCEPTION = 2;
	private static final int STATUS_EXIST_FILE = 3;

	@Inject
	private ArchivoService archivoService;

	@Inject
	private ArchivoFacturaService archivoFacturaService;

	@Inject
	private ArchivoHeaderService archivoHeaderService;

	@SuppressWarnings("finally")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/uploadFile")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public ArchivoResponseDTO uploadFile(@Multipart("file") InputStream file,
			@Multipart("fileName") String fileName,
			@Multipart("type") String type,
			@Multipart("idFactura") Long idFactura) {
		ArchivoResponseDTO response = new ArchivoResponseDTO();
		ArchivoFactura af = new ArchivoFactura();
		try {
			if (archivoHeaderService.isUnique(fileName, idFactura)) {
				af = archivoFacturaService.saveFile(fileName, type, idFactura);
				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				IOUtils.copy(file, bytes);
				archivoService.saveFile(bytes.toByteArray(),
						af.getArchivoHeader());
				response.setMessage("");
				response.setStatus(STATUS_OK);
			} else {
				response.setStatus(STATUS_EXIST_FILE);
			}
		} catch (IOException ex) {
			archivoFacturaService.delete(af);
			response.setMessage(ex.getMessage());
			response.setStatus(STATUS_DATABASE_EXCEPTION);
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
					dtoGet.setDate(ParseoService
							.getFormattedDate(archivoFactura.getArchivoHeader()
									.getCreatedAt()));
					dtoGet.setCanDelete(archivoHeaderService.canDelete(archivoFactura.getArchivoHeader().getId()));
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