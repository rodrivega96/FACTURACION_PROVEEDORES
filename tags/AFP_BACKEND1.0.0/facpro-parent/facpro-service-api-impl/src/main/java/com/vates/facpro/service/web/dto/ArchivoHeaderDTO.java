package com.vates.facpro.service.web.dto;

import java.util.List;

import lombok.Data;

@Data
public class ArchivoHeaderDTO {

	private Long sizeMax;
	private List<GetArchivoHeaderDTO> listArchivoHeader;

}
