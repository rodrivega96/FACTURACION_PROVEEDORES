package com.vates.facpro.service.web.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vates.facpro.business.states.oc.EnPreparacion;
import com.vates.facpro.business.states.oc.EstadoOC;
import com.vates.facpro.persistence.domain.ArchivoOrden;
import com.vates.facpro.persistence.domain.Cliente;
import com.vates.facpro.persistence.domain.EstadoPrefa;
import com.vates.facpro.persistence.domain.FacturaCliente;
import com.vates.facpro.persistence.domain.FacturasClientePorOC;
import com.vates.facpro.persistence.domain.ItemOrden;
import com.vates.facpro.persistence.domain.Manager;
import com.vates.facpro.persistence.domain.Moneda;
import com.vates.facpro.persistence.domain.OcProyecto;
import com.vates.facpro.persistence.domain.Orden;
import com.vates.facpro.persistence.domain.People;
import com.vates.facpro.persistence.domain.Propuesta;
import com.vates.facpro.persistence.domain.Proyecto;
import com.vates.facpro.persistence.domain.TipoOrden;
import com.vates.facpro.persistence.domain.Unidad;
import com.vates.facpro.persistence.domain.User;
import com.vates.facpro.persistence.domain.UserView;
import com.vates.facpro.persistence.util.ParseoService;
import com.vates.facpro.security.dto.UserDTO;
import com.vates.facpro.service.web.comparators.ItemOrdenDTOComparator;

public class ConversorDTO {

	public static OrdenCompraDTO getOrdenCompraDTO(Orden oc) {
		OrdenCompraDTO dto = new OrdenCompraDTO();
		dto.setId(oc.getId());
		dto.setActiva(oc.getActiva());
		dto.setClienteId(oc.getClienteId());
		dto.setConcepto(oc.getConcepto());
		dto.setContacto(oc.getContacto());
		dto.setCondicionPago(oc.getCondicionPago());
		dto.setEstado(oc.getEstado());
		dto.setFechaEmision(oc.getFechaEmision() != null ? ParseoService
				.getFormattedDate(oc.getFechaEmision()) : null);
		dto.setFechaVencimiento(oc.getFechaVencimiento() != null ? ParseoService
				.getFormattedDate(oc.getFechaVencimiento()) : null);
		dto.setMonedaId(oc.getMonedaId());
		dto.setNumero(oc.getNumero());
		dto.setTipo(oc.getTipo());
		dto.setTotal(oc.getTotal());
		dto.setVersion(oc.getVersion());
		List<ItemOrdenDTO> listItemDTO = new ArrayList<ItemOrdenDTO>();
		for (ItemOrden io : oc.getItemsOrden()) {
			ItemOrdenDTO itemDTO = new ItemOrdenDTO();
			itemDTO.setId(io.getId());
			itemDTO.setCantidad(io.getCantidad());
			itemDTO.setDescripcion(io.getDescripcion());
			itemDTO.setPrecio(io.getPrecio());
			itemDTO.setTotal(io.getTotal());
			itemDTO.setUnidad(io.getUnidad().getId());
			itemDTO.setExtendido(io.isExtendido());
			listItemDTO.add(itemDTO);
		}
		Collections.sort(listItemDTO, new ItemOrdenDTOComparator());
		dto.setItems(listItemDTO);
		return dto;
	}

	public static void getOrden(OrdenCompraDTO dto, Orden oc, UserDTO userDTO,
			Boolean force) {
		oc.setActiva(dto.getActiva());
		oc.setClienteId(dto.getClienteId());
		oc.setConcepto(dto.getConcepto());
		oc.setCondicionPago(dto.getCondicionPago());
		oc.setContacto(dto.getContacto());
		oc.setFechaEmision(dto.getFechaEmision() != null ? ParseoService
				.parseDate(dto.getFechaEmision()) : null);
		oc.setFechaVencimiento(dto.getFechaVencimiento() != null ? ParseoService
				.parseDate(dto.getFechaVencimiento()) : null);
		oc.setMonedaId(dto.getMonedaId());
		oc.setNumero(dto.getNumero());
		oc.setTipo(dto.getTipo());
		oc.setUpdatedBy(userDTO.getId());
		if (!force) {
			oc.setVersion(dto.getVersion());
		}
		if (oc.getId() == null) {
			oc.setEstado(dto.getEstado());
			oc.setTotal(dto.getTotal());
			oc.setSaldo(dto.getTotal());
			oc.setCreatedBy(userDTO.getId());
			oc.setItemsOrden(new HashSet<ItemOrden>());
			oc.setOcProyectos(new HashSet<OcProyecto>());
		}
		if (dto.getItems() != null) {
			List<ItemOrden> listItem = new ArrayList<ItemOrden>();
			Long index = 1L;
			for (ItemOrdenDTO itemDto : dto.getItems()) {
				boolean updateItem = false;
				if (itemDto.getId() != null) {
					for (ItemOrden itemO : oc.getItemsOrden()) {
						if (itemDto.getId().equals(itemO.getId())) {
							updateItem(itemO, itemDto);
							listItem.add(itemO);
							updateItem = true;
							break;
						}
					}
				}
				if (!updateItem) {
					ItemOrden item = new ItemOrden();
					item.setCantidad(itemDto.getCantidad());
					item.setDescripcion(itemDto.getDescripcion());
					item.setExtendido(itemDto.isExtendido());
					item.setPrecio(itemDto.getPrecio());
					item.setTotal(itemDto.getTotal());
					item.setIndice(index);
					Unidad unidad = new Unidad();
					unidad.setId(itemDto.getUnidad());
					item.setUnidad(unidad);
					listItem.add(item);
					index++;
				}
			}
			oc.getItemsOrden().clear();
			oc.getItemsOrden().addAll(listItem);
		}
		if (!dto.getProyectos().isEmpty()) {
			List<OcProyecto> listProyectos = new ArrayList<OcProyecto>();
			for (ProyectoDTO proyDTO : dto.getProyectos()) {
				OcProyecto ocProy = new OcProyecto();
				ocProy.setProyectoId(proyDTO.getId());
				ocProy.setExtendido(proyDTO.getExtendido());
				listProyectos.add(ocProy);
			}
			oc.getOcProyectos().clear();
			oc.getOcProyectos().addAll(listProyectos);
		}
	}

	public static List<EstadoDTO> getEstadosDTO(Set<EstadoOC> estados) {
		List<EstadoDTO> estadosDTO = new ArrayList<EstadoDTO>();
		for (EstadoOC estado : estados) {
			EstadoDTO dto = new EstadoDTO();
			dto.setId(estado.getEstadoId());
			dto.setNombre(estado.getNombre());
			estadosDTO.add(dto);
		}
		return estadosDTO;
	}

	public static List<ClienteDTO> getClienteDTO(List<Cliente> clientes) {
		List<ClienteDTO> clientesDTO = new ArrayList<ClienteDTO>();
		for (Cliente cli : clientes) {
			ClienteDTO dto = new ClienteDTO();
			dto.setId(cli.getId());
			dto.setValue(cli.getNombre());
			dto.setContacto(cli.getContacto());
			clientesDTO.add(dto);
		}
		return clientesDTO;
	}

	public static List<UnidadDTO> getUnidadesDTO(List<Unidad> unidades) {
		List<UnidadDTO> unidadesDTO = new ArrayList<UnidadDTO>();
		for (Unidad uni : unidades) {
			UnidadDTO dto = new UnidadDTO();
			dto.setId(uni.getId());
			dto.setNombre(uni.getNombre());
			unidadesDTO.add(dto);
		}
		return unidadesDTO;
	}

	public static List<TipoOrdenDTO> getTipoOrdenesDTO(List<TipoOrden> list) {
		List<TipoOrdenDTO> TipoOrdenesDTO = new ArrayList<TipoOrdenDTO>();
		for (TipoOrden to : list) {
			TipoOrdenDTO dto = new TipoOrdenDTO();
			dto.setId(to.getId());
			dto.setNombre(to.getNombre());
			TipoOrdenesDTO.add(dto);
		}
		return TipoOrdenesDTO;
	}

	public static List<MonedaDTO> getMonedaDTO(List<Moneda> monedas) {
		List<MonedaDTO> monedaDTO = new ArrayList<MonedaDTO>();
		for (Moneda moneda : monedas) {
			MonedaDTO dto = new MonedaDTO();
			dto.setId(moneda.getId());
			dto.setNombre(moneda.getNombre());
			monedaDTO.add(dto);
		}
		return monedaDTO;
	}

	public static void updateItem(ItemOrden itemOrden, ItemOrdenDTO itemDTO) {
		itemOrden.setCantidad(itemDTO.getCantidad());
		itemOrden.setDescripcion(itemDTO.getDescripcion());
		itemOrden.setExtendido(itemDTO.isExtendido());
		itemOrden.setPrecio(itemDTO.getPrecio());
		itemOrden.setTotal(itemDTO.getTotal());
		Unidad unidad = new Unidad();
		unidad.setId(itemDTO.getUnidad());
		itemOrden.setUnidad(unidad);
	}

	public static List<ArchivoOrdenDTO> getArchivoOrden(
			List<ArchivoOrden> archOrdenList, Long estado) {
		List<ArchivoOrdenDTO> archivosDTO = new ArrayList<ArchivoOrdenDTO>();
		for (ArchivoOrden archOrden : archOrdenList) {
			ArchivoOrdenDTO dto = new ArchivoOrdenDTO();
			dto.setDate(archOrden.getArchivoHeader().getCreatedAt() != null ? ParseoService
					.getFormattedDate(archOrden.getArchivoHeader()
							.getCreatedAt()) : null);
			dto.setName(archOrden.getArchivoHeader().getName());
			dto.setType(archOrden.getArchivoHeader().getFileType());
			dto.setId(archOrden.getArchivoHeader().getId());
			dto.setCanDelete(estado == EnPreparacion.STATE_ID ? true : false);
			archivosDTO.add(dto);
		}
		return archivosDTO;
	}

	public static List<ArchivoOrdenDTO> getArchivoOrden(
			List<ArchivoOrden> archOrdenList) {
		List<ArchivoOrdenDTO> archivosDTO = new ArrayList<ArchivoOrdenDTO>();
		for (ArchivoOrden archOrden : archOrdenList) {
			ArchivoOrdenDTO dto = new ArchivoOrdenDTO();
			dto.setDate(archOrden.getArchivoHeader().getCreatedAt() != null ? ParseoService
					.getFormattedDate(archOrden.getArchivoHeader()
							.getCreatedAt()) : null);
			dto.setName(archOrden.getArchivoHeader().getName());
			dto.setType(archOrden.getArchivoHeader().getFileType());
			dto.setId(archOrden.getArchivoHeader().getId());
			archivosDTO.add(dto);
		}
		return archivosDTO;
	}

	public static List<PropuestaDTO> getPropuestaDTO(
			List<Propuesta> propuestas, String seleccionado) {
		Long seleccionadoId = ParseoService.parseLong(seleccionado);
		List<PropuestaDTO> propuestasDTO = new ArrayList<PropuestaDTO>();
		for (Propuesta propuesta : propuestas) {
			PropuestaDTO dto = new PropuestaDTO();
			dto.setId(propuesta.getId());
			dto.setDescripcion(propuesta.getDescripcion());
			dto.setComercial(propuesta.getComercial());
			dto.setFechaDesde(ParseoService.getFormattedDate(propuesta
					.getVigenteDesde()));
			dto.setFechaHasta(ParseoService.getFormattedDate(propuesta
					.getVigenteHasta()));
			dto.setSeleccionado(propuesta.getId().equals(seleccionadoId));
			propuestasDTO.add(dto);
		}
		return propuestasDTO;
	}

	public static List<ProyectoDTO> getProyectoDTO(List<Proyecto> proyectos,
			String seleccionado) {
		Long seleccionadoId = ParseoService.parseLong(seleccionado);
		List<ProyectoDTO> proyectosDTO = new ArrayList<ProyectoDTO>();
		for (Proyecto proyecto : proyectos) {
			ProyectoDTO dto = new ProyectoDTO();
			dto.setId(proyecto.getId());
			dto.setNombre(proyecto.getNombre());
			dto.setActivo(getSiNo(proyecto.getActivo()));
			dto.setSeleccionado(proyecto.getId().equals(seleccionadoId));
			if (proyecto.getPmgId() != null) {
				dto.setPm(proyecto.getPmg());
			} else if (proyecto.getPmId() != null) {
				dto.setPm(proyecto.getPm());
			}
			dto.setCanDelete(true);
			proyectosDTO.add(dto);
		}
		return proyectosDTO;
	}

	public static String getSiNo(Integer param) {
		return param == 1 ? "SI" : "NO";
	}

	public static ProyectoDTO getProyectoDTO(Proyecto proy, OcProyecto ocProy,
			Long estado) {
		ProyectoDTO proyectoDTO = new ProyectoDTO();
		proyectoDTO.setId(ocProy.getProyectoId());
		proyectoDTO.setExtendido(ocProy.isExtendido());
		proyectoDTO.setNombre(proy.getNombre());
		if (proy.getPmgId() != null) {
			proyectoDTO.setPm(proy.getPmg());
		} else if (proy.getPmId() != null) {
			proyectoDTO.setPm(proy.getPm());
		}
		proyectoDTO.setSeleccionado(true);
		proyectoDTO.setCanDelete(estado.equals(EnPreparacion.STATE_ID));
		return proyectoDTO;
	}

	public static List<ManagerDTO> getManagerDTO(List<Manager> managers,
			String seleccionado) {
		Long seleccionadoId = ParseoService.parseLong(seleccionado);
		List<ManagerDTO> managerListDTO = new ArrayList<ManagerDTO>();
		for (Manager manager : managers) {
			ManagerDTO dto = new ManagerDTO();
			dto.setId(manager.getId());
			dto.setNombre(manager.getNombre());
			dto.setSeleccionado(manager.getId().equals(seleccionadoId));
			managerListDTO.add(dto);
		}
		return managerListDTO;
	}

	public static List<EstadoPreFacDTO> getEstadosPreFacDTO(
			List<EstadoPrefa> esatdosPreFac) {
		List<EstadoPreFacDTO> preFacDTO = new ArrayList<EstadoPreFacDTO>();
		for (EstadoPrefa estado : esatdosPreFac) {
			EstadoPreFacDTO dto = new EstadoPreFacDTO();
			dto.setId(estado.getId());
			dto.setNombre(estado.getNombre());
			preFacDTO.add(dto);
		}
		return preFacDTO;
	}

	// TODO
	public static List<FacturaClienteDTO> getFacturasList(
			List<FacturaCliente> facturasCliente) {
		List<FacturaClienteDTO> facturaListDTO = new ArrayList<FacturaClienteDTO>();
		for (FacturaCliente fac : facturasCliente) {
			FacturaClienteDTO dto = new FacturaClienteDTO();
			dto.setNumero(fac.getNumero());
			dto.setNroConformidad("Confor: " + fac.getNumero());
			dto.setTotal(fac.getTotal());
			facturaListDTO.add(dto);
		}
		return facturaListDTO;
	}

	public static List<PeoplePrefDTO> getPeoples(List<People> peoples) {
		List<PeoplePrefDTO> peopleListDTO = new ArrayList<PeoplePrefDTO>();
		for (People people : peoples) {
			PeoplePrefDTO dto = new PeoplePrefDTO();
			dto.setAmount(ParseoService.alwaysTwoDecimal(people.getImporte()));
			dto.setName(people.getPresona());
			peopleListDTO.add(dto);
		}
		return peopleListDTO;
	}

	public static FacturaClientePorOCDTO getFacturasByOrdenId(
			List<Long> facturasClientePorOCs,
			List<FacturaCliente> facturaClientes, Long ordenId) {
		FacturaClientePorOCDTO facPorOCDTO = new FacturaClientePorOCDTO();
		facPorOCDTO.setFacturaCliente(new ArrayList<FacturaClienteDTO>());
		facPorOCDTO.setOrdenId(ordenId);
		for (FacturaCliente fac : facturaClientes) {
			FacturaClienteDTO dto = new FacturaClienteDTO();
			dto.setFacturaId(fac.getId());
			dto.setNumero(fac.getNumero());
			dto.setSeleccionado(facturasClientePorOCs.contains(fac.getId()));
			dto.setTotal(fac.getTotal());
			facPorOCDTO.getFacturaCliente().add(dto);
		}
		return facPorOCDTO;
	}

	public static Map<Integer, Map<Long, FacturasClientePorOC>> getFacturaPorOC(
			List<FacturaClienteDTO> dtos, Long ordenId, Long usuarioId) {
		Map<Integer, Map<Long, FacturasClientePorOC>> map = new HashMap<Integer, Map<Long, FacturasClientePorOC>>();
		for (FacturaClienteDTO dto : dtos) {
			FacturasClientePorOC fcpoc = new FacturasClientePorOC();
			fcpoc.setTotal(dto.getTotal());
			fcpoc.setIdFactura(dto.getFacturaId());
			if (dto.getSeleccionado()) {
				Map<Long, FacturasClientePorOC> mapClientePorOCs = map
						.get(FacturasClientePorOC.SELECTED);
				if (mapClientePorOCs == null) {
					mapClientePorOCs = new HashMap<Long, FacturasClientePorOC>();
					map.put(FacturasClientePorOC.SELECTED, mapClientePorOCs);
				}
				fcpoc.setIdOrden(ordenId);
				fcpoc.setIdUsuario(usuarioId);
				mapClientePorOCs.put(dto.getFacturaId(), fcpoc);
			} else {
				Map<Long, FacturasClientePorOC> mapClientePorOCs = map
						.get(FacturasClientePorOC.NOT_SELECTED);
				if (mapClientePorOCs == null) {
					mapClientePorOCs = new HashMap<Long, FacturasClientePorOC>();
					map.put(FacturasClientePorOC.NOT_SELECTED, mapClientePorOCs);
				}
				mapClientePorOCs.put(dto.getFacturaId(), fcpoc);
			}
		}
		return map;
	}
	
	public static RoleDTO loadRole(Object[] role) {
		RoleDTO dto = new RoleDTO();
		dto.setRoleId((Long) role[0]);
		dto.setName((String) role[1]);
		dto.setDescription((String) role[2]);
		return dto;
	}

	public static void getUser(User user, UserView userView, Boolean force) {
		user.setActive(userView.getActive());
		user.setJob("".equals(userView.getJob()) ? " " : userView.getJob());
		user.setLastName(userView.getLastName());
		user.setName(userView.getName());
		user.setMail(userView.getMail());
		user.setTipo(userView.getTipo());
		if(!force){
			user.setVersion(userView.getVersion());			
		}
	}

}
