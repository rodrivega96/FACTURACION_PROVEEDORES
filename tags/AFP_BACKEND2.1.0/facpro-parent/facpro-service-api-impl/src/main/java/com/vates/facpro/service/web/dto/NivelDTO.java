package com.vates.facpro.service.web.dto;

import java.util.Date;

import lombok.Data;

import com.vates.facpro.persistence.domain.UserView;

@Data
public class NivelDTO {	
	private Long id;
	private Long facturaAdmId;
	private Integer orden;
	private UserView autorizador;
	
	private Integer estado;
	
	private Boolean publicado;
	private Boolean eliminado;
	private Long padre;
	
    private Date createdAt;
    private Date updatedAt;
    
    private Long estadoFactura;
}