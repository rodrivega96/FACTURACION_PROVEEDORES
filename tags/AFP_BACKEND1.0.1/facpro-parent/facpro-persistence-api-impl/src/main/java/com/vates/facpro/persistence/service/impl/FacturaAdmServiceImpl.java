package com.vates.facpro.persistence.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

import com.vates.facpro.business.states.AdministradorEstados;
import com.vates.facpro.business.states.Estados;
import com.vates.facpro.business.states.EstadosNivel;
import com.vates.facpro.persistence.comparators.NivelesComparator;
import com.vates.facpro.persistence.domain.ArchivoFactura;
import com.vates.facpro.persistence.domain.ArchivoHeader;
import com.vates.facpro.persistence.domain.Factura;
import com.vates.facpro.persistence.domain.FacturaAdm;
import com.vates.facpro.persistence.domain.FacturaAsientoAdm;
import com.vates.facpro.persistence.domain.FacturaView;
import com.vates.facpro.persistence.domain.Nivel;
import com.vates.facpro.persistence.domain.NivelPublicado;
import com.vates.facpro.persistence.domain.User;
import com.vates.facpro.persistence.paging.Page;
import com.vates.facpro.persistence.paging.PageConverter;
import com.vates.facpro.persistence.repository.ArchivoFacturaRepository;
import com.vates.facpro.persistence.repository.FacturaAdmRepository;
import com.vates.facpro.persistence.repository.TipoFacturaRepository;
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
	private ArchivoFacturaRepository archivoFacturaRepository;

	@PersistenceContext
	private EntityManager em;

	@Inject
	private PageConverter<FacturaAdm> pageConverter;

	@Inject
	private PageConverter<FacturaView> pageViewConverter;

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
	public Page<FacturaView> findPaginatedFilteredList(String numero,
			String tipo, String cuit, String razon, String cc, String centroc,
			String wf, String fvDesde, String fiDesde, String flfDesde,
			String fvHasta, String fiHasta, String flfHasta, String ffacDesde,
			String ffacHasta, String estado, int pageIndex, int rowsPerPage) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<FacturaAdm> query = builder.createQuery(FacturaAdm.class);
		CriteriaQuery<Long> queryCount = builder.createQuery(Long.class);
		Pageable pageable = constructPageSpecification(pageIndex, rowsPerPage);
		Root<FacturaAdm> fromFactura = query.from(FacturaAdm.class);
		Root<FacturaAdm> fromFacturaCount = queryCount.from(FacturaAdm.class);
		Set<Long> asientosId = getAsientosFilter(cc, centroc, pageable);
		fromFactura = getRestrictionsFacturaFilter(numero, tipo, cuit, razon,
				cc, centroc, wf, fvDesde, fiDesde, flfDesde, fvHasta, fiHasta,
				flfHasta, ffacDesde, ffacHasta, estado, query, fromFactura, asientosId);
		fromFacturaCount = getRestrictionsFacturaFilter(numero, tipo, cuit,
				razon, cc, centroc, wf, fvDesde, fiDesde, flfDesde, fvHasta,
				fiHasta, flfHasta, ffacDesde, ffacHasta, estado, queryCount, fromFacturaCount,
				asientosId);
		Long size = em.createQuery(
				queryCount.select(builder.count(fromFacturaCount)))
				.getSingleResult();
		

		BigDecimal bDLastPage = new BigDecimal((double)size/rowsPerPage);
		bDLastPage = bDLastPage.setScale(0, RoundingMode.UP);
		
		BigDecimal total = BigDecimal.ZERO;
		if ((pageIndex + 1) == bDLastPage.intValue()) { //ultima pagina
			List<FacturaAdm> resultImporteTotal = em.createQuery(query.select(fromFactura)).getResultList();
			for(FacturaAdm fAdm : resultImporteTotal) {
				for(FacturaAsientoAdm faAdm : fAdm.getAsientos()) {
					total = (total.add(new BigDecimal(faAdm.getImporteNeto().doubleValue()))).setScale(2,RoundingMode.HALF_UP);
				}
			}			
		}
					
		sortByNumeroDes(query, fromFactura);
		List<FacturaAdm> result = em
				.createQuery(query.select(fromFactura))
				.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
				.setMaxResults(pageable.getPageSize()).getResultList();
		List<FacturaView> viewResult = new ArrayList<FacturaView>();
		
		for (FacturaAdm adm : result) {
			FacturaView fv = loadView(adm);
			if ((pageIndex + 1) == bDLastPage.intValue()) {//ultima pagina
				fv.setImporteTotal(total.doubleValue());
			}
			
			viewResult.add(fv);
		}
		return getPageViewConverter().convertFrom(
				new PageImpl<FacturaView>(viewResult, pageable, size));
	}

	@Override
	public Page<FacturaView> findPaginatedPendingFilteredList(Long userId,
			int pageIndex, int rowsPerPage, String variable, Boolean order) {
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

		sortByVariableAndOrder(query, fromNivelPublicado, variable, order);

		Long size = em.createQuery(
				queryCount.select(builder.count(fromNivelPublicadoCount)))
				.getSingleResult();

		List<NivelPublicado> result = em
				.createQuery(query.select(fromNivelPublicado))
				.setFirstResult(
						pageable.getPageNumber() * pageable.getPageSize())
				.setMaxResults(pageable.getPageSize()).getResultList();

		List<FacturaView> viewResult = new ArrayList<FacturaView>();
		for (NivelPublicado np : result) {
			if (np.getFactura().getEstado() == Estados.EN_AUTORIZACION 
						&& !np.getFactura().getNivel().getUsuario().getId().equals(userId)) {
				viewResult.add(loadViewPendientesOK(np, userId));
			}
		}

		return getPageViewConverter().convertFrom(
				new PageImpl<FacturaView>(viewResult, pageable, size));
	}

	
	private <T, X> Root<T> getRestrictionsNivelesInferioresFilter(CriteriaQuery<X> query,
			Root<T> fromNivelPublicado, Long userId) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		Join<NivelPublicado, Nivel> nvlJoin = fromNivelPublicado.join(
				"niveles", JoinType.INNER);
		Join<Nivel, Factura> facJoin = nvlJoin.join("factura", JoinType.INNER);
		@SuppressWarnings("unused")
		Join<Factura, FacturaAdm> facAdmJoin = facJoin.join("facturaAdm",
				JoinType.INNER);
		Join<Factura,Nivel> facNivJoin = facJoin.join("nivel",JoinType.INNER);
		Join<Nivel,User> nivUsJoin = facNivJoin.join("usuario",JoinType.INNER);
		

		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(builder.equal(nvlJoin.<Long> get("usuario").get("id"),
				userId));
		predicates.add(builder.equal(fromNivelPublicado.<Boolean> get("last"),
				true));
		predicates.add(builder.equal(nvlJoin.<Long> get("estado"),
				Estados.INICIAL));
		predicates.add(builder.equal(facJoin.<Long> get("estado"),
				Estados.EN_AUTORIZACION));
		predicates.add(builder.notEqual(nivUsJoin.<Long> get("id"),
				userId));

		query.where(predicates.toArray(new Predicate[] {}));
		return fromNivelPublicado;
	}

	private FacturaView loadViewPendientesOK(NivelPublicado np, Long userId) {
		FacturaView fv = new FacturaView();

		fv.setIdFacturaAdm(np.getFactura().getFacturaAdm().getId());
		fv.setId(np.getFactura().getId());
		fv.setCuit(np.getFactura().getFacturaAdm().getCuit());
		fv.setRazonSocial(np.getFactura().getFacturaAdm().getRazonSocial());
		fv.setNroFactura(np.getFactura().getFacturaAdm().getNroFactura());
		fv.setDescripcion(np.getFactura().getFacturaAdm().getDescripcion());
		fv.setFechaFactura(ParseoService.getFormattedDate(np.getFactura()
				.getFacturaAdm().getFechaFactura()));

		fv.setDiasVencer(np.getFactura().getFacturaAdm().getId() != null ? calculateDay(
				np.getFactura().getVencimiento(), np.getFactura()
						.getTipoFactura()) : "");
		fv.setFechaVencimiento(np.getFactura().getVencimiento() != null ? ParseoService
				.getFormattedDate(np.getFactura().getVencimiento()) : "");
		fv.setImporteNeto(np.getFactura().getFacturaAdm().getGralBruto());
		fv.setImporteTotal(np.getFactura().getFacturaAdm().getGralNeto());
		fv.setIva(np.getFactura().getFacturaAdm().getGralIvaInc());

		List<Nivel> nivelesOrdenados = new ArrayList<Nivel>(np.getNiveles());
		Collections.sort(nivelesOrdenados, new NivelesComparator());
		List<Nivel> pendientes = new ArrayList<Nivel>();
		for (Nivel n : nivelesOrdenados) {
			if (!n.getEstado().equals(EstadosNivel.RECHAZADO)) {
				pendientes.add(n);
				if (n.getUsuario().getId().equals(userId)) {
					break;
				}
			}			
		}
		fv.setPendientesOK(pendientes);
		return fv;
	}

	private <T, X> Root<T> getRestrictionsFacturaFilter(String numero,
			String tipo, String cuit, String razon, String cuenta,
			String centroCosto, String wf, String fvDesde, String fiDesde,
			String flfDesde, String fvHasta, String fiHasta, String flfHasta,
			String ffacDesde, String ffacHasta, String estado,
			CriteriaQuery<X> query, Root<T> fromFactura, Set<Long> asientosId) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		Join<FacturaAdm, Factura> admJoin = null;
		Integer tipoInt = ParseoService.parseInteger(tipo);
		Integer wfInt = ParseoService.parseInteger(wf);
		Integer estadoInt = ParseoService.parseInteger(estado);
		Date fvDateDesde = ParseoService.parseDate(fvDesde);
		Date fiDateDesde = ParseoService.parseDate(fiDesde);
		Date flfDateDesde = ParseoService.parseDate(flfDesde);
		Date fvDateHasta = ParseoService.parseDate(fvHasta);
		Date fiDateHasta = ParseoService.parseDate(fiHasta);
		Date flfDateHasta = ParseoService.parseDate(flfHasta);
		Date ffacDateDesde = ParseoService.parseDate(ffacDesde);
		Date ffacDateHasta = ParseoService.parseDate(ffacHasta);
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
		if (estadoInt != null) {
			if (!new Integer(0).equals(estadoInt)) {
				predicates.add(builder.equal(admJoin.get("estado"), estadoInt));
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
				predicates.add(builder.not(builder.exists(subquery)));
			}
		}
		if (fvDateDesde != null) {
			predicates.add(builder.greaterThanOrEqualTo(
					admJoin.<Date> get("vencimiento"), fvDateDesde));
		}
		if (fvDateHasta != null) {
			predicates.add(builder.lessThanOrEqualTo(
					admJoin.<Date> get("vencimiento"), fvDateHasta));
		}
		if (flfDateDesde != null) {
			predicates.add(builder.greaterThanOrEqualTo(
					fromFactura.<Date> get("fechaContable"), flfDateDesde));
		}
		if (flfDateHasta != null) {
			predicates.add(builder.lessThanOrEqualTo(
					fromFactura.<Date> get("fechaContable"), flfDateHasta));
		}

		if (fiDateDesde != null) {
			predicates.add(builder.greaterThanOrEqualTo(
					fromFactura.<Date> get("fechaVencimiento"), fiDateDesde));
		}
		if (fiDateHasta != null) {
			predicates.add(builder.lessThanOrEqualTo(
					fromFactura.<Date> get("fechaVencimiento"), fiDateHasta));
		}
		if (ffacDateDesde != null) {
			predicates.add(builder.greaterThanOrEqualTo(
					fromFactura.<Date> get("fechaFactura"), ffacDateDesde));
		}
		if (ffacDateHasta != null) {
			predicates.add(builder.lessThanOrEqualTo(
					fromFactura.<Date> get("fechaFactura"), ffacDateHasta));
		}
		if (null == asientosId) {
			ArrayList<Long> listaAux = new ArrayList<Long>();
			listaAux.add(0L);
			predicates.add(fromFactura.get("id").in(listaAux));
		} else {
			if (!asientosId.isEmpty()) {
				predicates.add(fromFactura.get("id").in(asientosId));
			}
		}		

		predicates.add(builder.like(fromFactura.<String> get("nroFactura"),
				prepareString(numero)));
		predicates.add(builder.like(fromFactura.<String> get("cuit"),
				prepareString(cuit)));
		predicates.add(builder.like(fromFactura.<String> get("razonSocial"),
				prepareString(razon)));
		query.where(predicates.toArray(new Predicate[] {}));
		return fromFactura;
	}

	public Set<Long> getAsientosFilter(String cuenta, String centroCosto,
			Pageable pageable) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		Root<FacturaAsientoAdm> from = query.from(FacturaAsientoAdm.class);
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (cuenta != null && !cuenta.isEmpty()) {
			predicates.add(builder.like(from.<String> get("descripcionCuenta"),
					prepareString(cuenta)));
		}
		if (centroCosto != null && !centroCosto.isEmpty()) {
			try{
				Long nroCentroCostos = Long.parseLong(centroCosto);
				//Es numerico
				predicates.add(builder.equal(
						from.<String> get("nroCentroCostos"), nroCentroCostos));
			}catch(Exception e){
				//Es string
				predicates.add(builder.like(
						from.<String> get("descripcionCentroCostos"),
						prepareString(centroCosto)));
			}			
		} else {
			predicates.add(builder.like(from.<String> get("descripcionCentroCostos"), ""));
		}
		
		query.where(predicates.toArray(new Predicate[] {}));
		query.multiselect(from.<String> get("id"));
		List<Long> result = em
				.createQuery(query)
				.setFirstResult(
						pageable.getPageNumber() * pageable.getPageSize())
				.setMaxResults(pageable.getPageSize()).getResultList();
		
		if (result.isEmpty() && ((centroCosto != null && !centroCosto.isEmpty()) 
					|| (cuenta != null && !cuenta.isEmpty()))) {
			return null;			
		}
		return new HashSet<Long>(result);
	}

	@Override
	public Page<FacturaView> findPaginatedFilteredList(Long userId,
			int pageIndex, int rowsPerPage, String variable, Boolean order) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);
		CriteriaQuery<Long> queryCount = builder.createQuery(Long.class);

		Root<FacturaAdm> fromFacturaCount = queryCount.from(FacturaAdm.class);
		Root<FacturaAdm> fromFactura = query.from(FacturaAdm.class);

		Pageable pageable = constructPageSpecification(pageIndex, rowsPerPage);
		fromFactura = getRestrictionsFacturaFilterGroupBy(userId, query,
				fromFactura);

		sortByVariableAndOrder(query, fromFactura, variable, order);

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
		@SuppressWarnings("unused")
		Join<FacturaAdm, FacturaAsientoAdm> asientoJoin = fromFactura.join(
				"asientos", JoinType.INNER);
		Join<Factura, Nivel> nvlJoin = admJoin.join("nivel", JoinType.INNER);
		Join<Nivel, User> userJoin = nvlJoin.join("usuario", JoinType.INNER);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(builder.equal(userJoin.get("id"), userId));
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
		Join<Nivel, User> userJoin = nvlJoin.join("usuario", JoinType.INNER);

		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(builder.equal(userJoin.get("id"), userId));
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
		fromFactura.get("gralNeto"));
		query.where(predicates.toArray(new Predicate[] {}));

		return fromFactura;
	}

	private FacturaView loadView(FacturaAdm fa) {
		FacturaView fv = new FacturaView();
		fv.setCuit(fa.getCuit());
		fv.setFechaFactura(ParseoService.getFormattedDate(fa
				.getFechaFactura()));
		if (fa.getFactura() != null) {
			for (ArchivoFactura af : archivoFacturaRepository
					.findByFacturaId(fa.getFactura().getId())) {
				if(af.getArchivoHeader().getFileType().equals(ArchivoHeader.FACTURA) && !af.getArchivoHeader().getDeleted()){
					fv.setCanLoadWf(true);
					break;
				} else {
					fv.setCanLoadWf(false);
				}
			}
		}
		fv.setEstado(fa.getFactura() != null ? AdministradorEstados.estados
				.get(fa.getFactura().getEstado()).getNombre() : "Pendiente");
		fv.setIdFacturaAdm(fa.getId());
		fv.setNroFactura(fa.getNroFactura());
		fv.setRazonSocial(fa.getRazonSocial());
		fv.setTipoFactura(fa.getFactura() != null ? tfRepository.findOne(
				fa.getFactura().getTipoFactura()).getNombre() : null);
		fv.setVencimiento(fa.getFactura() != null ? ParseoService
				.getFormattedDate(fa.getFactura().getVencimiento()) : null);
		fv.setCanLoadFile(fa.getFactura() != null);
		fv.setFechaVencimiento(ParseoService.getFormattedDate(fa
				.getFechaVencimiento()));
		fv.setFechaContable(ParseoService.getFormattedDate(fa
				.getFechaContable()));
		fv.setId(fa.getFactura() != null ? fa.getFactura().getId() : null);
		fv.setDescripcion(fa.getDescripcion());
		fv.setDiasVencer(fa.getFactura() != null ? calculateDay(fa.getFactura()
				.getVencimiento(), fa.getFactura().getTipoFactura()) : "");
		fv.setWorkflow(fa.getFactura() != null
				&& !fa.getFactura().getNiveles().isEmpty() ? Factura.OPT_YES
				: Factura.OPT_NO);
		fv.setAutorizaciones(fa.getFactura() != null
				&& !fa.getFactura().getNiveles().isEmpty() ? calculateAutorizaciones(fa
				.getFactura().getNiveles()) : "");

		Double importeTotal = 0D;
		for (FacturaAsientoAdm asiento : fa.getAsientos()) {
			importeTotal += asiento.getImporteNeto();
		}
		fv.setImporteNeto(importeTotal);
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
		fv.setFechaFactura(ParseoService.getFormattedDate(ParseoService
				.parseDate(fa[8].toString())));
		fv.setImporteNeto((Double) fa[9]);
		fv.setIva((Double) fa[10]);
		fv.setImporteTotal((Double) fa[11]);
		fv.setFechaVencimiento(ParseoService.getFormattedDate((Date) fa[5]));
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
			Root<T> fromFactura, String variable, Boolean order) {
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
				result = "Faltan " + diff + " Días";
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
