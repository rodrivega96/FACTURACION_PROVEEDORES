package com.vates.facpro.persistence.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.business.states.oc.AdministradorEstadosOC;
import com.vates.facpro.business.states.oc.Cerrada;
import com.vates.facpro.business.states.oc.Vigente;
import com.vates.facpro.persistence.domain.Cliente;
import com.vates.facpro.persistence.domain.EstadoPrefa;
import com.vates.facpro.persistence.domain.FacturaCliente;
import com.vates.facpro.persistence.domain.FacturaClienteOC;
import com.vates.facpro.persistence.domain.FacturasClientePorOC;
import com.vates.facpro.persistence.domain.HistorialOC;
import com.vates.facpro.persistence.domain.Manager;
import com.vates.facpro.persistence.domain.Moneda;
import com.vates.facpro.persistence.domain.OcProyecto;
import com.vates.facpro.persistence.domain.Orden;
import com.vates.facpro.persistence.domain.OrdenPrefView;
import com.vates.facpro.persistence.domain.OrdenView;
import com.vates.facpro.persistence.domain.People;
import com.vates.facpro.persistence.domain.PreFacturacionView;
import com.vates.facpro.persistence.domain.Prefacturacion;
import com.vates.facpro.persistence.domain.Propuesta;
import com.vates.facpro.persistence.domain.Proyecto;
import com.vates.facpro.persistence.domain.ProyectoPropuesta;
import com.vates.facpro.persistence.domain.ProyectosOC;
import com.vates.facpro.persistence.domain.TipoOrden;
import com.vates.facpro.persistence.domain.Unidad;
import com.vates.facpro.persistence.paging.Page;
import com.vates.facpro.persistence.paging.PageConverter;
import com.vates.facpro.persistence.repository.ClienteRepository;
import com.vates.facpro.persistence.repository.EstadoPreFacRepository;
import com.vates.facpro.persistence.repository.FacturaClientePorOCRepository;
import com.vates.facpro.persistence.repository.FacturaClienteRepository;
import com.vates.facpro.persistence.repository.HistorialRepository;
import com.vates.facpro.persistence.repository.MonedaRepository;
import com.vates.facpro.persistence.repository.OrdenRepository;
import com.vates.facpro.persistence.repository.PeopleRepository;
import com.vates.facpro.persistence.repository.ProyectoRepository;
import com.vates.facpro.persistence.repository.TipoOrdenRepository;
import com.vates.facpro.persistence.repository.UnidadRepository;
import com.vates.facpro.persistence.service.OrdenService;
import com.vates.facpro.persistence.util.ParseoService;

/**
 * @author Lucas Scarlatta
 * 
 */
@Repository("ordenService")
@Transactional(propagation = Propagation.NESTED)
public class OrdenServiceImpl extends AbstractBaseService<Orden, Long>
		implements OrdenService {

	@Inject
	private OrdenRepository repository;
	
	@Inject
	private HistorialRepository historialRepository;

	@Inject
	private ProyectoRepository proyectoRepository;

	@Inject
	private TipoOrdenRepository tipoOrdenRepository;

	@Inject
	private UnidadRepository unidadRepository;

	@Inject
	private MonedaRepository monedaRepository;

	@Inject
	private ClienteRepository clienteRepository;

	@Inject
	private EstadoPreFacRepository estadoPreFacRepository;

	@Inject
	private FacturaClienteRepository facturaClienteRepository;

	@Inject
	private PeopleRepository peopleRepository;
	
	@Inject
	private FacturaClientePorOCRepository facturaClientePorOCRepository;

	@Inject
	private PageConverter<Orden> pageConverter;

	@Inject
	private PageConverter<OrdenView> pageViewConverter;

	@Inject
	private PageConverter<PreFacturacionView> pageViewConverterPF;

	@PersistenceContext
	private EntityManager em;

	@Override
	protected JpaRepository<Orden, Long> getRepository() {
		return repository;
	}

	@Override
	protected PageConverter<Orden> getPageConverter() {
		return pageConverter;
	}

	@Override
	public Orden saveOrden(Orden orden) {
		return repository.saveAndFlush(orden);
	}

	@Override
	public List<TipoOrden> getTiposOrdenes() {
		return tipoOrdenRepository.findAll();
	}

	@Override
	public List<Cliente> getClientes() {
		return clienteRepository.findAll();
	}

	@Override
	public List<Proyecto> getProyectos(String nombre, String activo, String pm,
			String propuestaId, String clienteId, String managerId) {
		Integer activoInt = ParseoService.parseInteger(activo);

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Proyecto> query = builder.createQuery(Proyecto.class);
		Root<Proyecto> fromProyecto = query.from(Proyecto.class);

		fromProyecto = getRestrictionsProyectoFilter(nombre, activoInt, pm,
				propuestaId, clienteId, managerId, query, fromProyecto);

		return em.createQuery(query.select(fromProyecto)).getResultList();

	}

	private <T, X> Root<T> getRestrictionsProyectoFilter(String nombre,
			Integer activo, String pm, String propuestaId, String clienteId,
			String managerId, CriteriaQuery<X> query, Root<T> fromProyecto) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();
		Long managerLong = ParseoService.parseLong(managerId);
		Long prupuestaLong = ParseoService.parseLong(propuestaId);
		if (activo != null) {
			predicates.add(builder.equal(fromProyecto.get("activo"), activo));
		}

		if (nombre != null && !"".equals(nombre)) {
			predicates.add(builder.like(
					builder.upper(fromProyecto.<String> get("nombre")),
					prepereQueryString(nombre).toUpperCase()));
		}

		if (pm != null && !"".equals(pm)) {
			List<Predicate> predicates1 = new ArrayList<Predicate>();
			List<Predicate> predicates2 = new ArrayList<Predicate>();
			predicates1.add(builder.isNotNull(fromProyecto.get("pmgId")));
			predicates1.add(builder.like(
					builder.upper(fromProyecto.<String> get("pmg")),
					prepereQueryString(pm).toUpperCase()));
			predicates2
					.add(builder.and(predicates1.toArray(new Predicate[] {})));
			predicates1 = new ArrayList<Predicate>();
			predicates1.add(builder.isNull(fromProyecto.get("pmgId")));
			predicates1.add(builder.like(
					builder.upper(fromProyecto.<String> get("pm")),
					prepereQueryString(pm).toUpperCase()));
			predicates2
					.add(builder.and(predicates1.toArray(new Predicate[] {})));
			predicates.add(builder.or(predicates2.toArray(new Predicate[] {})));
		}
		Long cliente = ParseoService.parseLong(clienteId);
		if (prupuestaLong != null && prupuestaLong != 0L) {
			Subquery<Long> subquery = query.subquery(Long.class);
			Root<ProyectoPropuesta> fromPPSubquery = subquery
					.from(ProyectoPropuesta.class);
			List<Predicate> subQueryPredicates = new ArrayList<Predicate>();
			subQueryPredicates.add(builder.equal(
					fromPPSubquery.get("propuestaId"), prupuestaLong));
			subQueryPredicates.add(builder.equal(
					fromPPSubquery.get("proyectoId"), fromProyecto.get("id")));
			subquery.where(subQueryPredicates.toArray(new Predicate[] {}));
			subquery.select(fromPPSubquery.<Long> get("proyectoId"));
			predicates.add(builder.exists(subquery));
		} else if (cliente != null && cliente != 0L) {
			predicates
					.add(builder.equal(fromProyecto.get("clienteId"), cliente));
		}

		if (managerLong != null && managerLong != 0L) {
			List<Predicate> predicates1 = new ArrayList<Predicate>();
			List<Predicate> predicates2 = new ArrayList<Predicate>();
			predicates1.add(builder.isNotNull(fromProyecto.get("pmgId")));
			predicates1.add(builder.equal(fromProyecto.<Long> get("pmgId"),
					managerLong));
			predicates2
					.add(builder.and(predicates1.toArray(new Predicate[] {})));
			predicates1 = new ArrayList<Predicate>();
			predicates1.add(builder.isNull(fromProyecto.get("pmgId")));
			predicates1.add(builder.equal(fromProyecto.<Long> get("pmId"),
					managerLong));
			predicates2
					.add(builder.and(predicates1.toArray(new Predicate[] {})));
			predicates.add(builder.or(predicates2.toArray(new Predicate[] {})));
		}
		query.where(predicates.toArray(new Predicate[] {}));
		return fromProyecto;
	}

	@Override
	public List<Propuesta> getPropuestas(String descripcion, String comercial,
			String fechaVigenciaDesde, String fechaVigenciaHasta,
			String clienteId) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Propuesta> query = builder.createQuery(Propuesta.class);
		Root<Propuesta> fromPropuesta = query.from(Propuesta.class);

		fromPropuesta = getRestrictionsPropuestaFilter(descripcion, comercial,
				fechaVigenciaDesde, fechaVigenciaHasta, clienteId, query,
				fromPropuesta);

		return em.createQuery(query.select(fromPropuesta)).getResultList();

	}

	private <T, X> Root<T> getRestrictionsPropuestaFilter(String descripcion,
			String comercial, String fechaVigenciaDesde,
			String fechaVigenciaHasta, String clienteId,
			CriteriaQuery<X> query, Root<T> fromPropuesta) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();
		Long cliente = ParseoService.parseLong(clienteId);
		if (fechaVigenciaDesde != null && !"".equals(fechaVigenciaDesde)) {
			predicates.add(builder.greaterThanOrEqualTo(
					fromPropuesta.<Date> get("vigenteDesde"),
					ParseoService.parseDate(fechaVigenciaDesde)));

		}
		if (fechaVigenciaHasta != null && !"".equals(fechaVigenciaHasta)) {
			predicates.add(builder.lessThanOrEqualTo(
					fromPropuesta.<Date> get("vigenteHasta"),
					ParseoService.parseDate(fechaVigenciaHasta)));

		}
		predicates.add(builder.like(
				builder.upper(fromPropuesta.<String> get("comercial")),
				prepereQueryString(comercial).toUpperCase()));
		predicates.add(builder.like(
				builder.upper(fromPropuesta.<String> get("descripcion")),
				prepereQueryString(descripcion).toUpperCase()));
		if (cliente != null && cliente != 0L) {
			predicates.add(builder.equal(fromPropuesta.get("clienteId"),
					cliente));
		}
		query.where(predicates.toArray(new Predicate[] {}));
		return fromPropuesta;
	}

	private String prepereQueryString(String param) {
		if (param == null) {
			param = "%";
		}
		return "%" + param + "%";
	}

	@Override
	public List<Unidad> getUnidades() {
		return unidadRepository.findAll();
	}

	@Override
	public Orden getOrden(Long ordenId) {
		return repository.findById(ordenId);
	}

	@Override
	public List<Moneda> getMonedas() {
		return monedaRepository.findAll();
	}

	@Override
	public Orden updateOrden(Orden orden, Double total, Long state) {
		HistorialOC historial = null;
		if ((orden.getEstado().equals(Vigente.STATE_ID) || orden.getEstado()
				.equals(Cerrada.STATE_ID)) && !orden.getTotal().equals(total)) {
			historial = AdministradorEstadosOC.cambiarEstado(orden,
					orden.getUpdatedBy());
		} else if (!state.equals(orden.getEstado())) {
			historial = AdministradorEstadosOC.cambiarEstado(orden, state,
					orden.getUpdatedBy());
		}
		if (historial != null) {
			historialRepository.save(historial);
		}
		orden.setSaldo(total - orden.getTotal() + orden.getSaldo());
		orden.setTotal(total);
		return repository.saveAndFlush(orden);
	}

	@Override
	public Orden updateOrdenChangeState(Orden orden, Long userId, Long estado) {
		HistorialOC hist = null;
		if (estado == null) {
			hist = AdministradorEstadosOC.cambiarEstado(orden, userId);
		} else {
			hist = AdministradorEstadosOC.cambiarEstado(orden, estado, userId);
		}
		historialRepository.save(hist);
		return repository.save(orden);

	}

	@Override
	public void setNumeroOCInterna(Orden orden) {
		StringBuffer sb = new StringBuffer();
		Object[] number = (Object[]) repository
				.findNumeroByTipoAndMaxFechaEmision(TipoOrden.INTERNA);
		Long prefix = (Long) (number == null ? 0L : number[0]);
		Long sufix = ((Long) (number == null ? 0L : number[1]));
		orden.setNumeroInternaPrefix(prefix);
		orden.setNumeroInternaSufix(++sufix);
		orden.setNumero(sb.append("OCI ").append(format("0000", prefix))
				.append("-").append(format("00000000", sufix)).toString());
	}

	@Override
	public Proyecto findProyectoById(Long proyectoId) {
		return proyectoRepository.findById(proyectoId);
	}
	
	@Override
	public List<Proyecto> findProyectosByIds(Set<Long> proyectoId) {
		return proyectoRepository.findByIdIn(proyectoId);
	}

	private String format(String pattern, Long value) {
		DecimalFormat formatter = new DecimalFormat(pattern);
		return formatter.format(value);
	}

	@Override
	public Boolean esUnica(String numero, Long tipo) {
		return repository.findByNumeroAndTipoAndActiva(numero, tipo,
				Orden.ACTIVA) == null;
	}

	public Page<OrdenView> findPaginatedFilteredList(String clienteNombre,
			String clienteId, String proyectoId, String propuestaId,
			String activa, String estado, int pageIndex, int rowsPerPage) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
		CriteriaQuery<Long> queryCount = builder.createQuery(Long.class);
		Pageable pageable = constructPageSpecification(pageIndex, rowsPerPage);
		Root<Orden> fromOC = query.from(Orden.class);
		Root<Orden> fromOCCount = queryCount.from(Orden.class);

		Integer activaInt = ParseoService.parseInteger(activa);
		Long proyecto = ParseoService.parseLong(proyectoId);
		Long propuesta = ParseoService.parseLong(propuestaId);
		Long cliente = ParseoService.parseLong(clienteId);
		Long estadoInt = ParseoService.parseLong(estado);
		fromOC = getRestrictionsOCFilter(clienteNombre, cliente, proyecto,
				propuesta, activaInt, estadoInt, query, fromOC, true);
		fromOCCount = getRestrictionsOCFilter(clienteNombre, cliente, proyecto,
				propuesta, activaInt, estadoInt, queryCount, fromOCCount, false);
		Long size = em.createQuery(
				queryCount.select(builder.count(fromOCCount)))
				.getSingleResult();
		sortByNumeroDes(query, fromOC);

		List<Object[]> result = em
				.createQuery(query)
				.setFirstResult(
						pageable.getPageNumber() * pageable.getPageSize())
				.setMaxResults(pageable.getPageSize()).getResultList();
		List<OrdenView> viewResult = new ArrayList<OrdenView>();
		for (Object[] orden : result) {
			OrdenView ov = loadView(orden);
			viewResult.add(ov);
		}
		return getPageViewConverter().convertFrom(
				new PageImpl<OrdenView>(viewResult, pageable, size));
	}

	private <T, X> Root<T> getRestrictionsOCFilter(String clienteNombre,
			Long clienteId, Long proyectoId, Long propuestaId, Integer activa,
			Long estado, CriteriaQuery<X> query, Root<T> fromOC,
			boolean multiselect) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder builder = em.getCriteriaBuilder();
		Root<Cliente> clienteFrom = query.from(Cliente.class);
		Root<ProyectosOC> proyectosFrom = query.from(ProyectosOC.class);
		Root<FacturaClienteOC> facturasFrom = query
				.from(FacturaClienteOC.class);
		predicates.add(builder.equal(fromOC.get("clienteId"),
				clienteFrom.get("id")));
		predicates.add(builder.equal(fromOC.get("id"),
				proyectosFrom.get("idOrden")));
		predicates.add(builder.equal(fromOC.get("id"),
				facturasFrom.get("idOrden")));
		if (activa != null) {
			predicates.add(builder.equal(fromOC.get("activa"), activa));
		}
		if (estado != null) {
			predicates.add(builder.equal(fromOC.get("estado"), estado));
		}
		if (clienteId != null) {
			predicates.add(builder.equal(fromOC.get("clienteId"), clienteId));
		} else if (!"".equals(clienteNombre)) {
			Subquery<Cliente> subquery = query.subquery(Cliente.class);
			Root<Cliente> fromClienteSubquery = subquery.from(Cliente.class);
			List<Predicate> subQueryPredicates = new ArrayList<Predicate>();

			subQueryPredicates.add(builder.equal(fromClienteSubquery.get("id"),
					fromOC.get("clienteId")));

			subQueryPredicates.add(builder.like(
					builder.upper(fromClienteSubquery.<String> get("nombre")),
					prepereQueryString(clienteNombre).toUpperCase()));

			subquery.where(subQueryPredicates.toArray(new Predicate[] {}));
			subquery.select(fromClienteSubquery);
			predicates.add(builder.exists(subquery));
		}

		if (proyectoId != 0L || propuestaId != 0L) {

			Subquery<Orden> subquery = query.subquery(Orden.class);
			Root<Orden> fromProyectoSubquery = subquery.from(Orden.class);
			Join<Orden, OcProyecto> ocpJoin = fromProyectoSubquery.join(
					"ocProyectos", JoinType.INNER);
			List<Predicate> subQueryPredicates = new ArrayList<Predicate>();
			subQueryPredicates.add(builder.equal(
					fromProyectoSubquery.get("id"), fromOC.get("id")));
			if (proyectoId != 0L) {
				subQueryPredicates.add(builder.equal(ocpJoin.get("proyectoId"),
						proyectoId));
			}
			if (propuestaId != 0L) {
				Root<ProyectoPropuesta> fromPPSubquery = subquery
						.from(ProyectoPropuesta.class);
				subQueryPredicates.add(builder.equal(
						fromPPSubquery.get("proyectoId"),
						ocpJoin.get("proyectoId")));
				subQueryPredicates.add(builder.equal(
						fromPPSubquery.get("propuestaId"), propuestaId));
			}
			subquery.where(subQueryPredicates.toArray(new Predicate[] {}));
			subquery.select(fromProyectoSubquery);
			predicates.add(builder.exists(subquery));
		}
		query.where(predicates.toArray(new Predicate[] {}));

		if (multiselect) {
			query.multiselect(fromOC.get("id"), fromOC.get("numero"),
					fromOC.get("estado"), fromOC.get("concepto"),
					fromOC.get("fechaEmision"), clienteFrom.get("nombre"),
					facturasFrom.get("facturaNro"),
					proyectosFrom.get("proyectos"), fromOC.get("activa"));

		}
		return fromOC;
	}

	private Pageable constructPageSpecification(int pageIndex, int rowsPerPage) {
		Pageable pageSpecification = new PageRequest(pageIndex, rowsPerPage);
		return pageSpecification;
	}

	private <X, T> void sortByNumeroDes(CriteriaQuery<X> query,
			Root<T> fromFactura) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		query.orderBy(builder.desc(fromFactura.get("id")));
	}

	private OrdenView loadView(Object[] orden) {
		OrdenView ov = new OrdenView();
		ov.setId((Long) orden[0]);
		ov.setNumero((String) orden[1]);
		ov.setEstadoId((Long) orden[2]);
		ov.setEstado(AdministradorEstadosOC.estados.get((Long) orden[2])
				.getNombre());
		ov.setConcepto((String) orden[3]);
		ov.setFechaEmision(ParseoService.getFormattedDate((Date) orden[4]));
		ov.setCliente((String) orden[5]);
		ov.setFacturas((String) orden[6]);
		ov.setProyecto((String) orden[7]);
		ov.setActiva(Orden.ACTIVA.equals((Long) orden[8]));
		return ov;
	}

	private OrdenView loadViewConsult(Object[] orden, Double total) {
		OrdenView ov = new OrdenView();
		ov.setId((Long) orden[0]);
		ov.setNumero((String) orden[1]);
		ov.setEstadoId((Long) orden[2]);
		ov.setEstado(AdministradorEstadosOC.estados.get((Long) orden[2])
				.getNombre());
		ov.setConcepto((String) orden[3]);
		ov.setFechaVencimiento(ParseoService.getFormattedDate((Date) orden[4]));
		ov.setCliente((String) orden[5]);
		ov.setClienteId((Long) orden[7]);
		ov.setMontoOC(ParseoService.alwaysTwoDecimal((Double) orden[6]));
		ov.setTotalFacturado(ParseoService.alwaysTwoDecimal(total));
		ov.setSaldoOC(ParseoService.alwaysTwoDecimal((Double) orden[8]));
		return ov;
	}

	protected PageConverter<OrdenView> getPageViewConverter() {
		return pageViewConverter;
	}

	protected PageConverter<PreFacturacionView> getPageViewConverterPF() {
		return pageViewConverterPF;
	}

	@Override
	public Page<OrdenView> findPaginatedFilteredConsultList(
			String clienteNombre, String clienteId, String fvDesde,
			String fvHasta, String proyectoId, String managerId, String numero,
			String estadoOC, int pageIndex, int rowsPerPage) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
		CriteriaQuery<Long> queryCount = builder.createQuery(Long.class);
		Pageable pageable = constructPageSpecification(pageIndex, rowsPerPage);
		Root<Orden> fromConsultOC = query.from(Orden.class);
		Root<Orden> fromConsultOCCount = queryCount.from(Orden.class);
		Long proyecto = ParseoService.parseLong(proyectoId);
		Long manager = ParseoService.parseLong(managerId);
		Long cliente = ParseoService.parseLong(clienteId);
		Long estadoOCInt = ParseoService.parseLong(estadoOC);
		Date fvDesdeDate = ParseoService.parseDate(fvDesde);
		Date fvHastaDate = ParseoService.parseDate(fvHasta);
		fromConsultOC = getRestrictionsConsultFilter(clienteNombre, cliente,
				fvDesdeDate, fvHastaDate, proyecto, manager, numero,
				estadoOCInt, query, fromConsultOC);
		fromConsultOCCount = getRestrictionsConsultFilter(clienteNombre,
				cliente, fvDesdeDate, fvHastaDate, proyecto, manager, numero,
				estadoOCInt, queryCount, fromConsultOCCount);
		Long size = em.createQuery(
				queryCount.select(builder.count(fromConsultOCCount)))
				.getSingleResult();
		sortByNumeroDes(query, fromConsultOC);
		List<Object[]> result = em
				.createQuery(query)
				.setFirstResult(
						pageable.getPageNumber() * pageable.getPageSize())
				.setMaxResults(pageable.getPageSize()).getResultList();
		List<Long> listIds = new ArrayList<Long>();
		for (Object[] o : result) {
			listIds.add((Long) o[0]);
		}
		Map<Long, Double> map = new HashMap<Long, Double>();
		if(!listIds.isEmpty()){
			List<Object[]> result1 = facturaClientePorOCRepository
					.findFacturaTotalAndOrdenId(listIds);
			for (Object[] o : result1) {
				map.put((Long) o[0], (Double) o[1]);
			}			
		}
		List<OrdenView> viewResult = new ArrayList<OrdenView>();
		for (Object[] orden : result) {
			OrdenView ov = loadViewConsult(orden,
					map.get(orden[0]) == null ? 0.0d : map.get(orden[0]));
			viewResult.add(ov);
		}
		return getPageViewConverter().convertFrom(
				new PageImpl<OrdenView>(viewResult, pageable, size));
	}

	private <T, X> Root<T> getRestrictionsConsultFilter(String clienteNombre, Long clienteId, Date fvDesde, Date fvHasta,
			Long proyectoId, Long managerId, String numero, Long estadoOC,
			CriteriaQuery<X> query, Root<T> fromOC) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();
		Root<Cliente> clienteFrom = query.from(Cliente.class);
		predicates.add(builder.equal(fromOC.get("clienteId"),
				clienteFrom.get("id")));
		if (fvDesde != null) {
			predicates.add(builder.greaterThanOrEqualTo(
					fromOC.<Date> get("fechaVencimiento"), fvDesde));
		}
		if (fvHasta != null) {
			predicates.add(builder.lessThanOrEqualTo(
					fromOC.<Date> get("fechaVencimiento"), fvHasta));
		}
		if (!"".equals(numero)) {
			predicates.add(builder.like(
					builder.upper(fromOC.<String> get("numero")),
					prepereQueryString(numero).toUpperCase()));
		}
		if (estadoOC != null) {
			predicates.add(builder.equal(fromOC.get("estado"), estadoOC));
		}
		if (clienteId != null) {
			predicates.add(builder.equal(fromOC.get("clienteId"), clienteId));
		} else if (!"".equals(clienteNombre)) {
			Subquery<Cliente> subqueryCliente = query.subquery(Cliente.class);
			Root<Cliente> fromClienteSubquery = subqueryCliente.from(Cliente.class);
			List<Predicate> subQueryPredicates = new ArrayList<Predicate>();

			subQueryPredicates.add(builder.equal(fromClienteSubquery.get("id"),
					fromOC.get("clienteId")));
			subQueryPredicates.add(builder.like(
					builder.upper(fromClienteSubquery.<String> get("nombre")),
					prepereQueryString(clienteNombre).toUpperCase()));
			subqueryCliente.where(subQueryPredicates.toArray(new Predicate[] {}));
			subqueryCliente.select(fromClienteSubquery);
			predicates.add(builder.exists(subqueryCliente));
		}
		if (proyectoId != 0L || managerId != 0L) {

			Subquery<Orden> subquery = query.subquery(Orden.class);
			Root<Orden> fromProyectoSubquery = subquery.from(Orden.class);
			Join<Orden, OcProyecto> ocpJoin = fromProyectoSubquery.join(
					"ocProyectos", JoinType.INNER);

			List<Predicate> subQueryPredicates = new ArrayList<Predicate>();
			subQueryPredicates.add(builder.equal(
					fromProyectoSubquery.get("id"), fromOC.get("id")));
			if (proyectoId != 0L) {
				subQueryPredicates.add(builder.equal(ocpJoin.get("proyectoId"),
						proyectoId));
			} else {
				if (managerId != 0L) {
					Subquery<Long> subqueryProy = query.subquery(Long.class);
					Root<Proyecto> fromProySubquery = subqueryProy
							.from(Proyecto.class);
					subqueryProy.select(fromProySubquery.<Long> get("id"));
					List<Predicate> predicates1 = new ArrayList<Predicate>();
					predicates1.add(builder.equal(
							fromProySubquery.<Long> get("pmgId"), managerId));
					predicates1.add(builder.equal(
							fromProySubquery.<Long> get("pmId"), managerId));
					subqueryProy.where(builder.or(predicates1
							.toArray(new Predicate[] {})));
					subQueryPredicates.add(builder.and(ocpJoin.get("proyectoId")
							.in(subqueryProy)));
				}
			}
			subquery.where(subQueryPredicates.toArray(new Predicate[] {}));
			subquery.select(fromProyectoSubquery);
			predicates.add(builder.exists(subquery));
		}
		query.where(predicates.toArray(new Predicate[] {}));
		query.multiselect(fromOC.get("id"), fromOC.get("numero"),
				fromOC.get("estado"), fromOC.get("concepto"),
				fromOC.get("fechaVencimiento"), clienteFrom.get("nombre"),
				fromOC.get("total"), clienteFrom.get("id"), fromOC.get("saldo"));
		return fromOC;
	}

	@Override
	public List<Manager> getManagers(String nombre) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Manager> query = builder.createQuery(Manager.class);
		Root<Manager> fromManager = query.from(Manager.class);
		fromManager = getRestrictionsManagerFilter(nombre, query, fromManager);
		return em.createQuery(query.select(fromManager)).getResultList();
	}

	private Root<Manager> getRestrictionsManagerFilter(String nombre,
			CriteriaQuery<Manager> query, Root<Manager> fromManager) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (nombre != null && !"".equals(nombre)) {
			predicates.add(builder.like(
					builder.upper(fromManager.<String> get("nombre")),
					prepereQueryString(nombre).toUpperCase()));
		}
		query.where(predicates.toArray(new Predicate[] {}));
		return fromManager;
	}

	@Override
	public Page<PreFacturacionView> findPaginatedFilteredPreFacList(
			String ocId, String estadoPreFac, int pageIndex, int rowsPerPage) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
		CriteriaQuery<Long> queryCount = builder.createQuery(Long.class);
		Pageable pageable = constructPageSpecification(pageIndex, rowsPerPage);
		Root<FacturasClientePorOC> fromConsultPF = query.from(FacturasClientePorOC.class);
		Root<FacturasClientePorOC> fromConsultPFCount = queryCount
				.from(FacturasClientePorOC.class);
		Long ocIdLong = ParseoService.parseLong(ocId);
		Long estadoPreFacInt = ParseoService.parseLong(estadoPreFac);
		
		fromConsultPFCount = getRestrictionsPreFactFilter(ocIdLong, estadoPreFacInt, queryCount, fromConsultPFCount);

		int size = em.createQuery(
				queryCount.select(builder.count(fromConsultPFCount)))
				.getResultList().size();
		List<PreFacturacionView> viewResult = new ArrayList<PreFacturacionView>();
		if(size>0){
			fromConsultPF = getRestrictionsPreFactFilter(ocIdLong, estadoPreFacInt, query, fromConsultPF);
			List<Object[]> result = em
					.createQuery(query)
					.setFirstResult(
							pageable.getPageNumber() * pageable.getPageSize())
					.setMaxResults(pageable.getPageSize()).getResultList();
			for (Object[] preFact : result) {
				PreFacturacionView ov = loadViewPreFac(preFact);
				viewResult.add(ov);
			}
		}
		return getPageViewConverterPF().convertFrom(
				new PageImpl<PreFacturacionView>(viewResult, pageable, size));
	}

	private <T, X> Root<T> getRestrictionsPreFactFilter(Long ocId, Long estadoPreFac,
			CriteriaQuery<X> query, Root<T> fromConsultPF) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder builder = em.getCriteriaBuilder();	
		Root<Prefacturacion> fromPrefa = query.from(Prefacturacion.class);		

		
		predicates.add(builder.equal(fromPrefa.<String>get("factura"),
				fromConsultPF.<String>get("idFactura")));

		if (estadoPreFac != null) {
			predicates.add(builder.equal(fromPrefa.get("estadoId"),
					estadoPreFac));
		}
		
		if (ocId != null) {
			predicates.add(builder.equal(fromConsultPF.get("idOrden"), ocId));
		}

		query.where(predicates.toArray(new Predicate[] {}));
		sortByNumeroDes(query, fromPrefa);
		query.multiselect(fromPrefa.get("id"), fromPrefa.get("estadoNombre"),
				fromPrefa.get("estadoId"), fromPrefa.get("proyectoNombre"),
				fromPrefa.get("clienteNombre"), fromPrefa.get("pm"),
				fromPrefa.get("descripcion"), fromPrefa.get("periodo"),
				fromPrefa.get("factura"), fromPrefa.get("saldo"));
		query.groupBy(fromPrefa.get("id"), fromPrefa.get("estadoNombre"),
				fromPrefa.get("estadoId"), fromPrefa.get("proyectoNombre"),
				fromPrefa.get("clienteNombre"), fromPrefa.get("pm"),
				fromPrefa.get("descripcion"), fromPrefa.get("periodo"),
				fromPrefa.get("factura"),fromPrefa.get("saldo"));
		return fromConsultPF;
	}

	private PreFacturacionView loadViewPreFac(Object[] preFact) {
		PreFacturacionView pf = new PreFacturacionView();
		pf.setId((Long) preFact[0]);
		pf.setEstado(preFact[1].toString());
		pf.setEstadoId((Long) preFact[2]);
		pf.setProyecto(preFact[3].toString());
		pf.setCliente(preFact[4].toString());
		pf.setManager(preFact[5].toString());
		pf.setDescripcion(preFact[6].toString());
		pf.setPeriodo(ParseoService.getFormattedDate((Date) preFact[7]));
		pf.setImporte(ParseoService.alwaysTwoDecimal((Double) preFact[9]));
		return pf;
	}

	@Override
	public List<EstadoPrefa> getEsatdosPreFac() {
		return estadoPreFacRepository.findAll();
	}

	@Override
	public List<FacturaCliente> findFacturasByOrdenId(Long ordenId) {
		return facturaClienteRepository.findFacturaByOrdenId(ordenId);
	}

	@Override
	public List<People> getPeoplesByPrefa(Long prefaId) {
		return peopleRepository.findByPrefaId(prefaId);
	}

	@Override
	public List<OrdenPrefView> getOrdenesPrefByPrefa(Long prefaId) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Orden> query = builder.createQuery(Orden.class);
		Root<Orden> fromOrden = query.from(Orden.class);
		fromOrden = getRestrictionsOrdenPrefa(prefaId, query, fromOrden);
		sortByNumeroDes(query, fromOrden);
		List<Orden> result = em.createQuery(query.select(fromOrden)).getResultList();
		List<OrdenPrefView> viewResult = new ArrayList<OrdenPrefView>();
		for (Orden orden : result) {
			OrdenPrefView opv = loadViewOrdenPrefView(orden);
			viewResult.add(opv);
		}
		return viewResult;
	}

	private OrdenPrefView loadViewOrdenPrefView(Orden orden) {
		OrdenPrefView opv = new OrdenPrefView();
		opv.setNro(orden.getNumero());
		opv.setConcept(orden.getConcepto());
		return opv;
	}

	private <T, X> Root<T> getRestrictionsOrdenPrefa(Long prefaId,
			CriteriaQuery<X> query, Root<T> fromOrden) {
		List<Predicate> predicates = new ArrayList<Predicate>();
//		CriteriaBuilder builder = em.getCriteriaBuilder();
//		Root<FacturasClientePorOC> fromFacturasPorOC = query.from(FacturasClientePorOC.class);
//		Root<Prefacturacion> fromPrefacturacion = query.from(Prefacturacion.class);
//		predicates.add(builder.equal(fromOrden.get("id"), fromFacturasPorOC.get("idOrden")));
//		predicates.add(builder.equal(fromOrden.get("clienteId"), fromPrefacturacion.get("clienteId")));
//		predicates.add(builder.equal(fromPrefacturacion.get("id"), prefaId));
		query.where(predicates.toArray(new Predicate[] {}));
		return fromOrden;
	}

	@Override
	public List<Long> findFacturaPorOCByOrdenId(Long ordenId) {
		return facturaClientePorOCRepository.findFacturaIdByOrdenId(ordenId);
	}

	@Override
	public List<FacturaCliente> findFacturasByClienteId(Long clienteId, Long idOrden) {
		return facturaClienteRepository.findByClienteIdAndNotFacturaPorOC(clienteId, idOrden);
	}

	@Override
	public void saveAsociarFactura(Map<Integer, Map<Long, FacturasClientePorOC>> map,
			Long ordenId) {
		List<FacturasClientePorOC> oldFacturasPorOC = facturaClientePorOCRepository
				.findByIdOrden(ordenId);
		List<FacturasClientePorOC> deleteList = new ArrayList<FacturasClientePorOC>();
		
		Double substract = 0.0d;
		
		Map<Long, FacturasClientePorOC> mapFacturasPorOC = map.get(FacturasClientePorOC.SELECTED);
		Map<Long, FacturasClientePorOC> mapFacturasPorOCNotSelected = map.get(FacturasClientePorOC.NOT_SELECTED);
		
		
		for (FacturasClientePorOC fcpoc : oldFacturasPorOC) {
			if (mapFacturasPorOC != null && mapFacturasPorOC.get(fcpoc.getIdFactura()) != null) {
				mapFacturasPorOC.remove(fcpoc.getIdFactura());
			} else if (mapFacturasPorOCNotSelected != null){
				FacturasClientePorOC fc = mapFacturasPorOCNotSelected.get(fcpoc.getIdFactura());
				if(fc != null){
					substract += fc.getTotal();					
				}
				deleteList.add(fcpoc);
			}
		}
		facturaClientePorOCRepository.delete(deleteList);
		if(mapFacturasPorOC != null){
			for(Long key : mapFacturasPorOC.keySet()){
				FacturasClientePorOC fcoc = mapFacturasPorOC.get(key);
				substract -= fcoc.getTotal();
				facturaClientePorOCRepository.save(fcoc);
			}			
		}
		repository.updateSaldo(substract, ordenId);
	}

}
