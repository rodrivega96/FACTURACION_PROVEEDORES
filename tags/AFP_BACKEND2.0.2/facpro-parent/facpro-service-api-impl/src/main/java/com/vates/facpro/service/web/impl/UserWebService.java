package com.vates.facpro.service.web.impl;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.persistence.OptimisticLockException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

import com.vates.facpro.persistence.domain.User;
import com.vates.facpro.persistence.paging.Page;
import com.vates.facpro.persistence.service.UserService;
import com.vates.facpro.service.web.dto.UserResponseDTO;

@Service("facpro.service.user")
@Path("/user")
public class UserWebService {

	private static final int STATUS_OK = 0;
	private static final int STATUS_OPTIMISTIC_EX = 1;
	private static final int STATUS_DATABASE_EXCEPTION = 2;
	private static final int STATUS_USER_EXIST = 3;
	private static final int STATUS_USER_DELETE = 4;
	private static final int STATUS_USER_INACTIVE = 5;
	private static final int STATUS_FAIL_MAIL = 6;

	@Inject
	private UserService userService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/userPaginatedList")
	@Consumes(MediaType.APPLICATION_JSON)
	public Page<User> getPaginatedList(final @QueryParam("name") String name,
			final @QueryParam("lastName") String lastName,
			final @QueryParam("active") int active,
			final @QueryParam("job") String job,
			final @QueryParam("page") int pageIndex,
			final @QueryParam("limit") int rowsPerPage) {

		return userService.findPaginatedFilteredList(name, lastName, active,
				job, pageIndex - 1, rowsPerPage);
	}

	@SuppressWarnings("finally")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getUser")
	@Consumes(MediaType.APPLICATION_JSON)
	public UserResponseDTO getUser(final @QueryParam("id") Long id) {
		UserResponseDTO response = new UserResponseDTO();
		try {
			response.setUser(userService.find(id));
			response.setStatus(STATUS_OK);
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
	@Path("/saveUser")
	@Consumes(MediaType.APPLICATION_JSON)
	public UserResponseDTO saveUser(User user) {
		UserResponseDTO response = new UserResponseDTO();
		try {
			if (!userService.existUser(user)) {
				userService.saveUser(user);
				response.setStatus(STATUS_OK);
			} else {
				response.setStatus(STATUS_USER_EXIST);
			}
			response.setMessage("");
		} catch (OptimisticLockException e) {
			response.setMessage(e.getMessage());
			response.setStatus(STATUS_OPTIMISTIC_EX);
		} catch (MessagingException e) {
			response.setMessage(e.getMessage());
			response.setStatus(STATUS_FAIL_MAIL);
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
	@Path("/updateUser")
	@Consumes(MediaType.APPLICATION_JSON)
	public UserResponseDTO updateUser(User user) {
		UserResponseDTO response = new UserResponseDTO();
		try {
			if (!userService.existUserUpdate(user)) {
				if (userService.existUserId(user.getId())) {
					userService.updateUser(user);
					response.setStatus(STATUS_OK);
				} else {
					response.setStatus(STATUS_USER_DELETE);
				}
			} else {
				response.setStatus(STATUS_USER_EXIST);
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

	@SuppressWarnings("finally")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/forceUpdateUser")
	@Consumes(MediaType.APPLICATION_JSON)
	public UserResponseDTO forceUpdateUser(User user) {
		UserResponseDTO response = new UserResponseDTO();
		try {
			if (!userService.existUserUpdate(user)) {
				if (userService.existUserId(user.getId())) {
					userService.forceUpdateUser(user);
					response.setStatus(STATUS_OK);
				} else {
					response.setStatus(STATUS_USER_DELETE);
				}
			} else {
				response.setStatus(STATUS_USER_EXIST);
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

	@SuppressWarnings("finally")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/deleteUser")
	@Consumes(MediaType.APPLICATION_JSON)
	public UserResponseDTO deleteUser(User user) {
		UserResponseDTO response = new UserResponseDTO();
		try {
			if (userService.existUser(user)) {
				if (userService.existWF(user.getId())) {
					user.setActive(User.INACTIVE);
					userService.updateUser(user);
					response.setStatus(STATUS_USER_INACTIVE);
				} else {
					userService.deleteUser(user);
					response.setStatus(STATUS_OK);
				}
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

	@SuppressWarnings("finally")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/forceDeleteUser")
	@Consumes(MediaType.APPLICATION_JSON)
	public UserResponseDTO forceDeleteUser(User user) {
		UserResponseDTO response = new UserResponseDTO();
		try {
			if (userService.existWF(user.getId())) {
				user.setActive(User.INACTIVE);
				userService.forceUpdateUser(user);
				response.setStatus(STATUS_USER_INACTIVE);
			} else {
				userService.forceDeleteUser(user);
				response.setStatus(STATUS_OK);
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
	
	
	@SuppressWarnings("finally")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getAuthorized")
	@Consumes(MediaType.APPLICATION_JSON)
	public UserResponseDTO getAuthorizersList() {
		UserResponseDTO response = new UserResponseDTO();
		try {
			response.setUserList(userService.getAuthorizersList());
			response.setStatus(STATUS_OK);
			
		} catch (Exception ex) {
			response.setMessage(ex.getMessage());
			response.setStatus(STATUS_DATABASE_EXCEPTION);
		} finally {
			return response;
		}
	}
}
