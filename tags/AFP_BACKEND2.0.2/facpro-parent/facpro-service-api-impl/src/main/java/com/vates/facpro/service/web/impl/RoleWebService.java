package com.vates.facpro.service.web.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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

import com.vates.facpro.persistence.domain.Role;
import com.vates.facpro.persistence.service.RoleService;
import com.vates.facpro.persistence.service.UserService;
import com.vates.facpro.service.web.dto.RoleDTO;
import com.vates.facpro.service.web.dto.RoleResponseDTO;

@Service("facpro.service.role")
@Path("/role")
public class RoleWebService {

	private static final int STATUS_OK = 0;
	private static final int STATUS_OPTIMISTIC_EX = 1;
	private static final int STATUS_DATABASE_EXCEPTION = 2;
	private static final int STATUS_USER_DELETE = 4;

	@Inject
	private RoleService roleService;

	@Inject
	private UserService userService;

	@SuppressWarnings("finally")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getRolesByUserId")
	@Consumes(MediaType.APPLICATION_JSON)
	public RoleResponseDTO getAllRoles(final @QueryParam("id") Long userId) {
		RoleResponseDTO response = new RoleResponseDTO();
		try {
			if (userService.existUserId(userId)) {
				response.setUserId(userId);
				response.setRoles(new ArrayList<RoleDTO>());
				for (Role role : roleService.findAllRole()) {
					RoleDTO dto = new RoleDTO();
					dto.setRoleId(role.getId());
					dto.setName(role.getName());
					dto.setDescription(role.getDescription());
					if (userService.existUserIdAndRolesId(userId, role.getId())) {
						dto.setSelected(true);
					}
					response.getRoles().add(dto);
				}
				response.setStatus(STATUS_OK);
			} else {
				response.setStatus(STATUS_USER_DELETE);
			}
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
	@Path("/saveRoles")
	@Consumes(MediaType.APPLICATION_JSON)
	public RoleResponseDTO saveRoles(RoleResponseDTO roleResponseDTO) {
		RoleResponseDTO response = new RoleResponseDTO();
		try {
			if (userService.existUserId(roleResponseDTO.getUserId())) {
				Set<Role> auxSetRoles = new HashSet<Role>();
				for (RoleDTO roleDTO : roleResponseDTO.getRoles()) {
					if (roleDTO.isSelected()) {
						Role newRol = roleService.find(roleDTO.getRoleId());
						auxSetRoles.add(newRol);
					}
				}
				roleService.saveRoles(roleResponseDTO.getUserId(), auxSetRoles);
				response.setStatus(STATUS_OK);
			} else {
				response.setStatus(STATUS_USER_DELETE);
			}
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
