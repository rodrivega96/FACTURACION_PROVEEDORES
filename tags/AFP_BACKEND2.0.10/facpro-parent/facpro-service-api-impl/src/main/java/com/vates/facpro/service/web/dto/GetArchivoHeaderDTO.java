package com.vates.facpro.service.web.dto;

import lombok.Data;

@Data
public class GetArchivoHeaderDTO {

	private Long id;
	private String name;
	private String date;
	private String typeFile;
	private Integer typeFileId;
	private Boolean canDelete;

}
