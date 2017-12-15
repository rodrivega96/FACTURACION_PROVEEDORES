package com.vates.facpro.persistence.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
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

import com.vates.facpro.business.states.factura.AdministradorEstados;
import com.vates.facpro.business.states.factura.Estados;
import com.vates.facpro.business.states.factura.EstadosNivel;
import com.vates.facpro.persistence.comparators.NivelesComparator;
import com.vates.facpro.persistence.domain.ArchivoHeader;
import com.vates.facpro.persistence.domain.Factura;
import com.vates.facpro.persistence.domain.FacturaAdm;
import com.vates.facpro.persistence.domain.FacturaAsientoAdm;
import com.vates.facpro.persistence.domain.FacturaView;
import com.vates.facpro.persistence.domain.Nivel;
import com.vates.facpro.persistence.domain.NivelPublicado;
import com.vates.facpro.persistence.domain.NivelView;
import com.vates.facpro.persistence.paging.Page;
import com.vates.facpro.persistence.paging.PageConverter;
import com.vates.facpro.persistence.repository.ArchivoFacturaRepository;
import com.vates.facpro.persistence.repository.CuentasExcluidasRepository;
import com.vates.facpro.persistence.repository.FacturaAdmRepository;
import com.vates.facpro.persistence.repository.NivelRepository;
import com.vates.facpro.persistence.repository.TipoFacturaRepository;
import com.vates.facpro.persistence.repository.UserRepository;
import com.vates.facpro.persistence.service.FacturaAdmService;
import com.vates.facpro.persistence.util.ParseoService;

/**
 * @author Cabrera Manuel
 * 
 */
@Repository("facturaAdmService")
@Transactional(propagation = Propagation.NESTED)
public class FacturaAdmServiceImpl extends
		AbstractBaseService<FacturaAdm, Long> implements FacturaAdmService {

	@Inject
	private FacturaAdmRepository repository;

	@Inject
	private TipoFacturaRepository tfRepository;
	
	@Inject
	private FacturaAdmRepository facturaAdmRepository;

	@Inject
	private ArchivoFacturaRepository archivoFacturaRepository;
	
	@Inject
	private CuentasExcluidasRepository cuentasExcluidasRepository;
	
	@Inject
	private UserRepository userRepository;

	@PersistenceContext
	private EntityManager em;

	@Inject
	private PageConverter<FacturaAdm> pageConverter;

	@Inject
	private PageConverter<FacturaView> pageViewConverter;
	
	@Inject
	private NivelRepository nivelRepository;

	@Override
	public void saveFactura(FacturaAdm factura) {
		repository.save(factura);

	}

	@Override
	protected JpaRepository<FacturaAdm, Long> getRepository() {
		return repository;
	}

	@Override
	protected PageConverter<FacturaAdm> getPageConverter() {
		return pageConverter;
	}

	protected PageConverter<FacturaView> getPageViewConverter() {
		return pageViewConverter;
	}

	@Override
	public FacturaAdm find(long id) {
		// TODO Auto-generated method stub
		return repository.findOne(id);
	}
	
	@Override
	public FacturaAdm findById(Long id){
		return repository.findById(id);
	}
	
	@Override
	public Set<String> findJerarquiasExcluidas(){
		return cuentasExcluidasRepository.findJerarquiasExcluidas();
	}

	@Override
	public Page<FacturaView> findPaginatedFilteredList(String numero,
			String tipo, String cuit, String razon, String cc, String centroc,
			String wf, String fvDesde, String fiDesde, String flfDesde,
			String fvHasta, String fiHasta, String flfHasta, String ffacDesde,
			String ffacHasta, String estado, int pageIndex, int rowsPerPage, Boolean allFacturas, Long userId) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<FacturaAdm> query = builder.createQuery(FacturaAdm.class);
		CriteriaQuery<Object[]> queryCount = builder.createQuery(Object[].class);
		Pageable pageable = constructPageSpecification(pageIndex, rowsPerPage);
		Root<FacturaAdm> fromFactura = query.from(FacturaAdm.class);
		Root<FacturaAdm> fromFacturaCount = queryCount.from(FacturaAdm.class);
		fromFactura = getRestrictionsFacturaFilter(numero, tipo, cuit, razon,
				cc, centroc, wf, fvDesde, fiDesde, flfDesde, fvHasta, fiHasta,
				flfHasta, ffacDesde, ffacHasta, estado, query, fromFactura, allFacturas, userId);
		fromFacturaCount = getRestrictionsFacturaFilterCount(numero, tipo, cuit,
				razon, cc, centroc, wf, fvDesde, fiDesde, flfDesde, fvHasta,
				fiHasta, flfHasta, ffacDesde, ffacHasta, estado, queryCount, fromFacturaCount, allFacturas, userId);
		List<Object[]> countTotal = em.createQuery(queryCount)
				.getResultList();
		Double size =  0.0;
		Double total = 0.0;
		for(Object[] ob :countTotal){
			size++;
			total+=(Double)ob[1];
		}
		Double lastPage = Math.floor(rowsPerPage>0?(size/rowsPerPage):size);
				
		sortByNumeroDes(query, fromFactura);
		List<FacturaAdm> result = em
				.createQuery(query.select(fromFactura))
				.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
				.setMaxResults(pageable.getPageSize()).getResultList();
		List<FacturaView> viewResult = new ArrayList<FacturaView>();
		
		for (FacturaAdm adm : result) {
			viewResult.add(loadView(adm));
		}
		Page<FacturaView> page = getPageViewConverter().convertFrom(
				new PageImpl<FacturaView>(viewResult, pageable, size.longValue()));
		page.setTotalAmount((pageIndex) == lastPage.intValue()?ParseoService.roundTwoDecimal(total):null);
		return  page;
	}
	
	@Override
	public Page<FacturaView> findPaginatedAdminFilteredList(String numero,
			String tipo, String cuit, String razon, String cc, String centroc,
			String wf, String fvDesde, String fiDesde, String flfDesde,
			String fvHasta, String fiHasta, String flfHasta, String ffacDesde,
			String ffacHasta, String estado, int pageIndex, int rowsPerPage, Boolean allFacturas, Long userId) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<FacturaAdm> query = builder.createQuery(FacturaAdm.class);
		CriteriaQuery<Long> queryCount = builder.createQuery(Long.class);
		Pageable pageable = constructPageSpecification(pageIndex, rowsPerPage);
		Root<FacturaAdm> fromFactura = query.from(FacturaAdm.class);
		Root<FacturaAdm> fromFacturaCount = queryCount.from(FacturaAdm.class);
		fromFactura = getRestrictionsFacturaFilter(numero, tipo, cuit, razon,
				cc, centroc, wf, fvDesde, fiDesde, flfDesde, fvHasta, fiHasta,
				flfHasta, ffacDesde, ffacHasta, estado, query, fromFactura, allFacturas, userId);
		fromFacturaCount = getRestrictionsFacturaFilter(numero, tipo, cuit,
				razon, cc, centroc, wf, fvDesde, fiDesde, flfDesde, fvHasta,
				fiHasta, flfHasta, ffacDesde, ffacHasta, estado, queryCount, fromFacturaCount, allFacturas, userId);
		Long size = em.createQuery(
				queryCount.select(builder.count(fromFacturaCount)))
				.getSingleResult();		
		sortByNumeroDes(query, fromFactura);
		List<FacturaAdm> result = em
				.createQuery(query.select(fromFactura))
				.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
				.setMaxResults(pageable.getPageSize()).getResultList();
		List<FacturaView> viewResult = new ArrayList<FacturaView>();
		
		for (FacturaAdm adm : result) {		
			viewResult.add(loadBasicView(adm));
		}
		return getPageViewConverter().convertFrom(
				new PageImpl<FacturaView>(viewResult, pageable, size));
	}

	@Override
	public Page<FacturaView> findPaginatedPendingFilteredList(Long userId,
			int pageIndex, int rowsPerPage, String variable, String variableLocal, Boolean order) {
		Pageable pageable = constructPageSpecification(pageIndex, rowsPerPage);
		CriteriaBuilder builder = em.getCriteriaBuilder();

		CriteriaQuery<NivelPublicado> query = builder
				.createQuery(NivelPublicado.class);
		Root<NivelPublicado> fromNivelPublicado = query
				.from(NivelPublicado.class);
		fromNivelPublicado = getRestrictionsNivelesInferioresFilter(query,
				fromNivelPublicado, userId);

		CriteriaQuery<Long> queryCount = builder.createQuery(Long.class);
		Root<NivelPublicado> fromNivelPublicadoCount = queryCount
				.from(NivelPublicado.class);
		fromNivelPublicadoCount = getRestrictionsNivelesInferioresFilter(queryCount,
				fromNivelPublicadoCount, userId);

		sortByVariableAndOrder(query, fromNivelPublicado, variable, variableLocal, order);

		Long size = em.createQuery(
				queryCount.select(builder.count(fromNivelPublicadoCount)))
				.getSingleResult();

		List<NivelPublicado> result = em
				.createQuery(query.select(fromNivelPublicado))
				.setFirstResult(
						pageable.getPageNumber() * pageable.getPageSize())
				.setMaxResults(pageable.getPageSize()).getResultList();

		List<FacturaView> viewResult = new ArrayList<FacturaView>();
		List<NivelPublicado> niveles = new ArrayList<NivelPublicado>();
		List<Long> idsFacturas = new ArrayList<Long>();
		for (NivelPublicado np : result) {
			if (np.getFactura().getEstado() == Estados.EN_AUTORIZACION 
						&& !np.getFactura().getNivel().getUsuarioId().equals(userId)) {
				niveles.add(np);
				idsFacturas.add(np.getFactura().getFacturaAdm());
			}
		}
		if (!niveles.isEmpty()) {
			List<FacturaAdm> facturasAdm = facturaAdmRepository
					.findByIdIn(idsFacturas);
			for (NivelPublicado np : niveles) {
				viewResult.add(loadViewPendientesOK(
						np,
						userId,
						getFacturaAdm(facturasAdm, np.getFactura()
								.getFacturaAdm())));
			}
		}

		return getPageViewConverter().convertFrom(
				new PageImpl<FacturaView>(viewResult, pageable, size));
	}
	
	private FacturaAdm getFacturaAdm(List<FacturaAdm> facturasAdm, Long facturaAdmId) {
		for (FacturaAdm fAdm : facturasAdm) {
			if (fAdm.getId().equals(facturaAdmId)) {
				return fAdm;
			}
		}
		return null;
	}

	
	private <T, X> Root<T> getRestrictionsNivelesInferioresFilter(CriteriaQuery<X> query,
			Root<T> fromNivelPublicado, Long userId) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		Join<NivelPublicado, Nivel> nvlJoin = fromNivelPublicado.join(
				"niveles", JoinType.INNER);
		Join<Nivel, Factura> facJoin = nvlJoin.join("factura", JoinType.INNER);
		Join<Factura,Nivel> facNivJoin = facJoin.join("nivel",JoinType.INNER);
		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(builder.equal(nvlJoin.<Long> get("usuarioId"),
				userId));
		predicates.add(builder.notEqual(facNivJoin.<Long> get("usuarioId"),
				userId));
		predicates.add(builder.equal(fromNivelPublicado.<Boolean> get("last"),
				true));
		predicates.add(builder.equal(nvlJoin.<Long> get("estado"),
				Estados.INICIAL));
		predicates.add(builder.equal(facJoin.<Long> get("estado"),
				Estados.EN_AUTORIZACION));

		query.where(predicates.toArray(new Predicate[] {}));
		return fromNivelPublicado;
	}

	private FacturaView loadViewPendientesOK(NivelPublicado np, Long userId, FacturaAdm facturaAdm) {
		FacturaView fv = new FacturaView();
        //FacturaAdm facturaAdm = facturaAdmRepository.findById(np.getFactura().getFacturaAdm());
		fv.setIdFacturaAdm(facturaAdm.getId());
		fv.setId(np.getFactura().getId());
		fv.setCuit(facturaAdm.getCuit());
		fv.setRazonSocial(facturaAdm.getRazonSocial());
		fv.setNroFactura(facturaAdm.getNroFactura());
		fv.setDescripcion(facturaAdm.getDescripcion());
		fv.setFechaFactura(ParseoService.getFormattedDate(facturaAdm.getFechaFactura()));

		fv.setDiasVencer(facturaAdm.getId() != null ? calculateDay(
				np.getFactura().getVencimiento(), np.getFactura()
						.getTipoFactura()) : "");
		fv.setFechaVencimiento(np.getFactura().getVencimiento() != null ? ParseoService
				.getFormattedDate(np.getFactura().getVencimiento()) : "");
		fv.setImporteNeto(facturaAdm.getGralBruto());
		fv.setImporteTotal(facturaAdm.getGralNeto());
		fv.setIva(facturaAdm.getGralIvaInc());

		List<Nivel> nivelesOrdenados = new ArrayList<Nivel>(np.getNiveles());
		Collections.sort(nivelesOrdenados, new NivelesComparator());
		List<NivelView> pendientes = new ArrayList<NivelView>();
		for (Nivel n : nivelesOrdenados) {
			if (!n.getEstado().equals(EstadosNivel.RECHAZADO)) {
				NivelView nvl = new NivelView();
				nvl.setOrden(n.getOrden());
				nvl.setMail(userRepository.findMailById(n.getUsuarioId()));
				pendientes.add(nvl);
				if (n.getUsuarioId().equals(userId)) {
					break;
				}
			}			
		}
		fv.setPendientesOK(pendientes);
		fv.setObservacion(facturaAdm.getFactura().getObservacion());
		return fv;
	}
	
	private <T, X> Root<T> getRestrictionsFacturaFilterCount(String numero,
			String tipo, String cuit, String razon, String cuenta,
			String centroCosto, String wf, String fvDesde, String fiDesde,
			String flfDesde, String fvHasta, String fiHasta, String flfHasta,
			String ffacDesde, String ffacHasta, String estado,
			CriteriaQuery<X> query, Root<T> fromFactura,
			boolean allFacturas, Long userId){
		CriteriaBuilder builder = em.getCriteriaBuilder();
		Root<T> from = getRestrictionsFacturaFilter(numero, tipo, cuit,
				razon, cuenta, centroCosto, wf, fvDesde, fiDesde, flfDesde, fvHasta,
				fiHasta, flfHasta, ffacDesde, ffacHasta, estado, query, fromFactura, allFacturas, userId);
		Join<FacturaAdm, FacturaAsientoAdm> asJoin = fromFactura.join("asientos", JoinType.INNER);
		query.multiselect(builder.count(getAtt("id", fromFactura)), builder.sum(asJoin.<Double> get("importeNeto")));
		query.groupBy(getAtt("id", fromFactura));
		return from;
	}
	

	private <T, X> Root<T> getRestrictionsFacturaFilter(String numero,
			String tipo, String cuit, String razon, String cuenta,
			String centroCosto, String wf, String fvDesde, String fiDesde,
			String flfDesde, String fvHasta, String fiHasta, String flfHasta,
			String ffacDesde, String ffacHasta, String estado,
			CriteriaQuery<X> query, Root<T> fromFactura,
			boolean allFacturas, Long userId) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		Join<FacturaAdm, Factura> admJoin = null;
		Integer tipoInt = ParseoService.parseInteger(tipo);
		Integer wfInt = ParseoService.parseInteger(wf);
		Integer estadoInt = ParseoService.parseInteger(estado);
		admJoin = fromFactura.join("factura", JoinType.LEFT);
		List<Predicate> predicates = new ArrayList<Predicate>();

		if (wfInt != null) {
			if (new Integer("1").equals(wfInt)) {
				predicates.add(builder.isNotEmpty(admJoin
						.<Collection<Nivel>> get("niveles")));
			} else {
				predicates.add(builder.isEmpty(admJoin
						.<Collection<Nivel>> get("niveles")));
			}
		}	
		if (tipoInt != null) {
			predicates.add(builder.equal(admJoin.get("tipoFactura"), tipoInt));
		}
		setDate(fvDesde, predicates, builder, admJoin, "vencimiento", false);
		setDate(fvHasta, predicates, builder, admJoin, "vencimiento", true);
		setDate(flfDesde, predicates, builder, fromFactura, "fechaContable", false);
		setDate(flfHasta, predicates, builder, fromFactura, "fechaContable", true);
		setDate(fiDesde, predicates, builder, fromFactura, "fechaVencimiento", false);
		setDate(fiHasta, predicates, builder, fromFactura, "fechaVencimiento", true);
		setDate(ffacDesde, predicates, builder, fromFactura, "fechaFactura", false);		
		setDate(ffacHasta, predicates, builder, fromFactura, "fechaFactura", true);
		
		setEstadoFilter(estadoInt, query, fromFactura, predicates, admJoin);
		setAsientosFilter(cuenta, centroCosto, query, fromFactura, predicates);
		
		setAdminFilter(query, fromFactura, userId, allFacturas, predicates);

		predicates.add(builder.like(fromFactura.<String> get("nroFactura"),
				prepareString(numero)));
		predicates.add(builder.like(fromFactura.<String> get("cuit"),
				prepareString(cuit)));
		predicates.add(builder.like(builder.upper(fromFactura.<String> get("razonSocial")),
				prepareString(razon.toUpperCase())));
		query.where(predicates.toArray(new Predicate[] {}));
		return fromFactura;
	}
	
	private <T> void setDate(String fecha, List<Predicate> predicatesParent,
			CriteriaBuilder builder, Root<T> fromFactura, String field, boolean less) {
		Date fechaDate = ParseoService.parseDate(fecha);
		if (fechaDate != null) {
			predicatesParent.add(less?builder.lessThanOrEqualTo(
					fromFactura.<Date> get(field), fechaDate):builder.greaterThanOrEqualTo(
							fromFactura.<Date> get(field), fechaDate));
		}
	}
	
	private <T, X> void setDate(String fecha, List<Predicate> predicatesParent,
			CriteriaBuilder builder, Join<T, X> join, String field, boolean less) {
		Date fechaDate = ParseoService.parseDate(fecha);
		if (fechaDate != null) {
			predicatesParent.add(less ? builder.lessThanOrEqualTo(
					join.<Date> get(field), fechaDate): builder.greaterThanOrEqualTo(
							join.<Date> get(field), fechaDate));
		}
	}
	
	private <T, X> void setAdminFilter(CriteriaQuery<X> query,
			Root<T> fromFactura, Long userId, Boolean allFacturas,
			List<Predicate> predicatesParent) {
		if (!allFacturas && userId != null) {
			CriteriaBuilder builder = em.getCriteriaBuilder();
			Subquery<Factura> subquery = query.subquery(Factura.class);
			Root<NivelPublicado> fromNivelSubquery = subquery
					.from(NivelPublicado.class);
			List<Predicate> subQueryPredicates = new ArrayList<Predicate>();
			Join<NivelPublicado, Factura> fromFacturaSubquery = fromNivelSubquery
					.join("factura", JoinType.INNER);

			Join<NivelPublicado, Nivel> nvlJoin = fromNivelSubquery.join(
					"niveles", JoinType.INNER);
			subQueryPredicates.add(builder.equal(
					nvlJoin.<Long> get("usuarioId"), userId));
			subQueryPredicates.add(builder.equal(
					fromFacturaSubquery.get("facturaAdm"),
					fromFactura.get("id")));
			subquery.where(subQueryPredicates.toArray(new Predicate[] {}));
			subquery.select(fromFacturaSubquery);
			predicatesParent.add(builder.exists(subquery));
		}
	}
	
	private <T, X, M, Z> void setEstadoFilter(Integer estadoInt, CriteriaQuery<X> query, Root<T> fromFactura,
			List<Predicate> predicatesParent, Join<Z, M> admJoin){
		if (estadoInt != null) {
			CriteriaBuilder builder = em.getCriteriaBuilder();
			if (!new Integer(0).equals(estadoInt)) {
				predicatesParent.add(builder.equal(admJoin.get("estado"), estadoInt));
			} else {
				Subquery<Factura> subquery = query.subquery(Factura.class);
				Root<Factura> fromFacturaSubquery = subquery
						.from(Factura.class);
				List<Predicate> subQueryPredicates = new ArrayList<Predicate>();
				subQueryPredicates.add(builder.equal(
						fromFacturaSubquery.get("facturaAdm"),
						fromFactura.get("id")));
				subquery.where(subQueryPredicates.toArray(new Predicate[] {}));
				subquery.select(fromFacturaSubquery);
				predicatesParent.add(builder.not(builder.exists(subquery)));
			}
		}
	}
	private <T, X> void setAsientosFilter(String cuenta, String centroCosto,
			CriteriaQuery<X> query, Root<T> fromFactura,
			List<Predicate> predicatesParent) {
		if ((centroCosto != null && !"".equals(centroCosto))
				|| (cuenta != null && !"".equals(cuenta))) {
			CriteriaBuilder builder = em.getCriteriaBuilder();
			Subquery<FacturaAdm> subquery = query.subquery(FacturaAdm.class);
			Root<FacturaAdm> fromAdm = subquery.from(FacturaAdm.class);
			Join<FacturaAdm, FacturaAsientoAdm> from = fromFactura.join(
					"asientos", JoinType.INNER);
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(builder.equal(fromAdm.get("id"),
					fromFactura.get("id")));
			if (cuenta != null && !"".equals(cuenta)) {
				predicates.add(builder.like(builder.upper(
						from.<String> get("descripcionCuenta")),
						prepareString(cuenta.toUpperCase())));
			}
			if (centroCosto != null && !"".equals(centroCosto)) {
				try {
					Long nroCentroCostos = Long.parseLong(centroCosto);
					// Es numerico
					predicates.add(builder.equal(
							from.<String> get("nroCentroCostos"),
							nroCentroCostos));
				} catch (Exception e) {
					// Es string
					predicates.add(builder.like(builder.upper(
							from.<String> get("descripcionCentroCostos")),
							prepareString(centroCosto.toUpperCase())));
				}
			}

			subquery.where(predicates.toArray(new Predicate[] {}));
			subquery.select(fromAdm);
			predicatesParent.add(builder.exists(subquery));
		}
	}

	@Override
	public Page<FacturaView> findPaginatedFilteredList(Long userId,
			int pageIndex, int rowsPerPage, String variable, String variableLocal, Boolean order) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
		CriteriaQuery<Long> queryCount = builder.createQuery(Long.class);

		Root<FacturaAdm> fromFacturaCount = queryCount.from(FacturaAdm.class);
		Root<FacturaAdm> fromFactura = query.from(FacturaAdm.class);

		Pageable pageable = constructPageSpecification(pageIndex, rowsPerPage);
		fromFactura = getRestrictionsFacturaFilterGroupBy(userId, query,
				fromFactura);

		sortByVariableAndOrder(query, fromFactura, variable, variableLocal, order);

		fromFacturaCount = getRestrictionsFacturaFilter(userId, queryCount,
				fromFacturaCount);

		long size = em
				.createQuery(queryCount.select(builder.count(fromFacturaCount)))
				.getResultList().size();

		List<Object[]> result = em
				.createQuery(query)
				.setFirstResult(
						pageable.getPageNumber() * pageable.getPageSize())
				.setMaxResults(pageable.getPageSize()).getResultList();

		List<FacturaView> viewResult = new ArrayList<FacturaView>();
		for (Object[] adm : result) {
			viewResult.add(loadView(adm));
		}

		return getPageViewConverter().convertFrom(
				new PageImpl<FacturaView>(viewResult, pageable, size));
	}

	private <T, X> Root<T> getRestrictionsFacturaFilter(Long userId,
			CriteriaQuery<X> query, Root<T> fromFactura) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		Join<FacturaAdm, Factura> admJoin = null;

		admJoin = fromFactura.join("factura", JoinType.INNER);
		Join<Factura, Nivel> nvlJoin = admJoin.join("nivel", JoinType.INNER);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(builder.equal(nvlJoin.get("usuarioId"), userId));
		predicates.add(builder.equal(admJoin.get("estado"),
				Estados.EN_AUTORIZACION));

		query.multiselect(builder.count(getAtt("id", fromFactura)));
		query.where(predicates.toArray(new Predicate[] {}));
		query.groupBy(getAtt("id", fromFactura));

		return fromFactura;
	}

	private <T, X> Root<T> getRestrictionsFacturaFilterGroupBy(Long userId,
			CriteriaQuery<X> query, Root<T> fromFactura) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		Join<FacturaAdm, Factura> admJoin = null;

		admJoin = fromFactura.join("factura", JoinType.INNER);
		Join<Factura, Nivel> nvlJoin = admJoin.join("nivel", JoinType.INNER);
		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(builder.equal(nvlJoin.get("usuarioId"), userId));
		predicates.add(builder.equal(admJoin.get("estado"),
				Estados.EN_AUTORIZACION));

		query.multiselect(
		admJoin.get("id"),
		fromFactura.get("id"),
		fromFactura.get("cuit"),
		fromFactura.get("razonSocial"),
		fromFactura.get("nroFactura"),
		admJoin.get("vencimiento"),
		admJoin.get("tipoFactura"),
		fromFactura.get("descripcion"),
		fromFactura.get("fechaFactura"),
		fromFactura.get("gralBruto"),
		fromFactura.get("gralIvaInc"),
		fromFactura.get("gralNeto"), 
		admJoin.get("observacion"));
		query.where(predicates.toArray(new Predicate[] {}));

		return fromFactura;
	}
	
	private FacturaView loadBasicView(FacturaAdm fa) {
		FacturaView fv = new FacturaView();
		fv.setNroFactura(fa.getNroFactura());
		fv.setRazonSocial(fa.getRazonSocial());
		fv.setCuit(fa.getCuit());
		fv.setVencimiento(fa.getFactura() != null ? ParseoService
				.getFormattedDate(fa.getFactura().getVencimiento()) : null);
		fv.setFechaFactura(ParseoService.getFormattedDate(fa
				.getFechaFactura()));
		fv.setWorkflow(fa.getFactura() != null
				&& nivelRepository.countByFacturaId(fa.getFactura().getId()) > 0 ? Factura.OPT_YES
				: Factura.OPT_NO);
		fv.setEstado(fa.getFactura() != null ? AdministradorEstados.estados
				.get(fa.getFactura().getEstado()).getNombre() : "Pendiente");
		
		fv.setTipoFactura(fa.getFactura() != null ? tfRepository
				.findNameById(fa.getFactura().getTipoFactura()) : null);
		fv.setCanLoadFile(fa.getFactura() != null);
		if (fa.getFactura() != null) {
			fv.setCanLoadWf(archivoFacturaRepository
					.countByFacturaIdAndArchivoHeaderDeletedAndArchivoHeaderFileType(
							fa.getFactura().getId(), false,
							ArchivoHeader.FACTURA)>0);
		}
		fv.setIdFacturaAdm(fa.getId());
		fv.setId(fa.getFactura() != null ? fa.getFactura().getId() : null);
		return fv;
	}

	private FacturaView loadView(FacturaAdm fa) {
		FacturaView fv = new FacturaView();
		fv.setCuit(fa.getCuit());
		fv.setNroFactura(fa.getNroFactura());
		fv.setRazonSocial(fa.getRazonSocial());
		fv.setDescripcion(fa.getDescripcion());
		fv.setDiasVencer(fa.getFactura() != null ? calculateDay(fa.getFactura()
				.getVencimiento(), fa.getFactura().getTipoFactura()) : "");
		Double importeTotal = 0D;
		for (FacturaAsientoAdm asiento : fa.getAsientos()) {
			importeTotal += asiento.getImporteNeto();
		}
		fv.setImporteNeto(importeTotal);
		fv.setEstado(fa.getFactura() != null ? AdministradorEstados.estados
				.get(fa.getFactura().getEstado()).getNombre() : "Pendiente");
		List<Nivel> niveles = fa.getFactura() != null ? nivelRepository
				.findNivelByFacturaId(fa.getFactura().getId()) : null;
		fv.setAutorizaciones(niveles != null && !niveles.isEmpty() ? calculateAutorizaciones(new HashSet<Nivel>(
				niveles)) : "");
		fv.setIdFacturaAdm(fa.getId());
		fv.setId(fa.getFactura() != null ? fa.getFactura().getId() : null);
		if (fa.getFactura() != null) {
			fv.setCanLoadWf(archivoFacturaRepository
					.countByFacturaIdAndArchivoHeaderDeletedAndArchivoHeaderFileType(
							fa.getFactura().getId(), false,
							ArchivoHeader.FACTURA)>0);
		}
		fv.setWorkflow(niveles != null
				&& !niveles.isEmpty() ? Factura.OPT_YES
				: Factura.OPT_NO);
		return fv;
	}

	private FacturaView loadView(Object[] fa) {
		FacturaView fv = new FacturaView();
		fv.setIdFacturaAdm((long) fa[1]);
		fv.setId((Long) fa[0]);
		fv.setCuit(fa[2].toString());
		fv.setRazonSocial(fa[3].toString());
		fv.setNroFactura(fa[4].toString());
		fv.setDiasVencer(fa[0] != null ? calculateDay((Date) fa[5],
				(Long) fa[6]) : "");
		fv.setDescripcion(fa[7].toString());
		fv.setFechaFactura(ParseoService.getFormattedDate((Date) fa[8]));
		fv.setImporteNeto((Double) fa[9]);
		fv.setIva((Double) fa[10]);
		fv.setImporteTotal((Double) fa[11]);
		fv.setFechaVencimiento(ParseoService.getFormattedDate((Date) fa[5]));
		fv.setObservacion(fa[12] != null ? fa[12].toString() : null);
		return fv;
	}

	public String prepareString(String input) {
		StringBuffer result = new StringBuffer();
		result.append("%");
		result.append(input == null ? "" : input);
		result.append("%");
		return result.toString();
	}

	private Pageable constructPageSpecification(int pageIndex, int rowsPerPage) {
		Pageable pageSpecification = new PageRequest(pageIndex, rowsPerPage);
		return pageSpecification;
	}

	@SuppressWarnings("unchecked")
	private <X, T> void sortByVariableAndOrder(CriteriaQuery<X> query,
			Root<T> fromFactura, String variable, String variableLocal ,Boolean order) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		if (variable == null || "".equals(variable)) {
			query.orderBy(builder.asc(getAtt("tipoFactura", fromFactura)),
					builder.asc(getAtt("vencimiento", fromFactura)));
		} else if ("importeNeto".equals(variable)) {
			Expression<Double> exp = (Expression<Double>) getAtt(variable,
					fromFactura);
			if (order) {
				query.orderBy(builder.asc(builder.sum(exp)));
			} else {
				query.orderBy(builder.desc(builder.sum(exp)));
			}
		} else if ("diaVencimiento".equals(variableLocal)) {
			if (order) {
				query.orderBy(builder.asc(getAtt("tipoFactura", fromFactura)),
						builder.asc(getAtt(variable, fromFactura)));
			}else {
				query.orderBy(builder.desc(getAtt("tipoFactura", fromFactura)),
						builder.desc(getAtt(variable, fromFactura)));					
			}
		} else {
			if (order) {
				query.orderBy(builder.asc(getAtt(variable, fromFactura)));
			} else {
				query.orderBy(builder.desc(getAtt(variable, fromFactura)));
			}
		}
	}

	private <T> Expression<?> getAtt(String variable, Root<T> fromFactura) {
		Expression<?> ob = null;
		try {
			ob = fromFactura.get(variable);
		} catch (Exception e) {
			ob = getAttInJoins(variable, fromFactura.getJoins());
		}
		return ob;
	}

	private <T> Expression<?> getAttInJoins(String variable,
			Set<Join<T, ?>> joins) {
		Expression<?> ob = null;
		Iterator<Join<T, ?>> it = joins.iterator();

		while (it.hasNext()) {
			Join<?, ?> j = (Join<?, ?>) it.next();
			try {
				return j.get(variable);
			} catch (Exception ex) {
				Expression<?> ob1 = null;
				if (!j.getJoins().isEmpty()) {
					ob1 = getAttInJoins(variable, j.getJoins());
					if (ob1 != null)
						return ob1;
				} else {
					return ob;
				}
			}
		}
		return ob;
	}

	private <X, T> void sortByNumeroDes(CriteriaQuery<X> query,
			Root<T> fromFactura) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		query.orderBy(builder.desc(fromFactura.get("id")));
	}

	private String calculateDay(Date fechaVencimietno, Long tipo) {
		String result = "";
		if (tipo.equals(FacturaAdm.A_VENCER)) {
			Date now = new Date();
			Long diff = Math.abs((now.getTime() - fechaVencimietno.getTime())
					/ (1000 * 60 * 60 * 24));
			if (now.compareTo(fechaVencimietno) < 0) {
				result = "Faltan " + ++diff + " Días";
			} else {
				result = "Venció hace " + diff + " Días";
			}
		} else {
			result = "Sin vencimiento";
		}

		return result;
	}

	private String calculateAutorizaciones(Set<Nivel> niveles) {
		String result = "";
		short autorizados = 0;
		short noEliminados = 0;
		for (Nivel n : niveles) {
			if (n.getEliminado() == false && !n.getEstado().equals(EstadosNivel.RECHAZADO)) {
				noEliminados++;
				if (n.getEstado() == EstadosNivel.AUTORIZADO) {
					autorizados++;
				}
			}
		}
		result = autorizados + "/" + noEliminados;
		return result;
	}

}
