package com.vates.facpro.service.web.dto;

import lombok.Data;

@Data
public class ArchivoOrdenDTO {

	private String name;
	private String date;
	private Integer type;
	private Long id;
	private Boolean canDelete;
	private String idTmp;
}
