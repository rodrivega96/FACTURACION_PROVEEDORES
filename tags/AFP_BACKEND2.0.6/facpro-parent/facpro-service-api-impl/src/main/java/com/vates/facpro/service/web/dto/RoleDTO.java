package com.vates.facpro.service.web.dto;

import lombok.Data;

@Data
public class RoleDTO {
	private Long roleId;
	private boolean selected;
	private String name;
	private String description;
}
