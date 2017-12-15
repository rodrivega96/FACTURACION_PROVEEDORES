package com.vates.facpro.persistence.service.impl;

import java.util.ArrayList;
import java.util.List;

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

import com.vates.facpro.persistence.domain.MedioPago;
import com.vates.facpro.persistence.domain.Proveedor;
import com.vates.facpro.persistence.domain.ProveedorView;
import com.vates.facpro.persistence.domain.TipoTelefono;
import com.vates.facpro.persistence.paging.Page;
import com.vates.facpro.persistence.paging.PageConverter;
import com.vates.facpro.persistence.repository.MedioPagoRepository;
import com.vates.facpro.persistence.repository.ProveedorRepository;
import com.vates.facpro.persistence.repository.TipoTelefonoRepository;
import com.vates.facpro.persistence.service.ProveedorService;
import com.vates.facpro.persistence.util.ParseoService;

/**
 * @author Cabrera Manuel
 * 
 */
@Repository("proveedorService")
@Transactional(propagation = Propagation.NESTED)
public class ProveedorServiceImpl extends AbstractBaseService<Proveedor, Long> implements
		ProveedorService {

	@Inject
	private ProveedorRepository repository;
	
	@Inject
	private TipoTelefonoRepository tipoTelefonoRepository;
	
	@Inject
	private MedioPagoRepository medioPagoRepository;
	

	@Inject
	private PageConverter<Proveedor> pageConverter;
	
	@Inject
	private PageConverter<ProveedorView> pageViewConverter;
	
	@PersistenceContext
	private EntityManager em;


	@Override
	protected JpaRepository<Proveedor, Long> getRepository() {
		return repository;
	}

	@Override
	protected PageConverter<Proveedor> getPageConverter() {
		return pageConverter;
	}
	
	protected PageConverter<ProveedorView> getPageViewConverter() {
		return pageViewConverter;
	}
	
	public Proveedor findById(long id){
		return repository.findByIdAndDeleted(id, false);
	}
	
	public Proveedor saveProveedor(Proveedor proveedor){
		return repository.save(proveedor);
	}
	
	public Proveedor saveProveedorView(ProveedorView proveedorView, Long userId) {
		Proveedor proveedorSave = null;
		if (proveedorView != null && proveedorView.getId() != null
				&& !"".equals(proveedorView.getId())) {
			proveedorSave = repository.findById(Long.parseLong(proveedorView
					.getId()));
			updateProveedor(proveedorSave, proveedorView, userId);
		} else {
			proveedorSave = createProveedor(proveedorView, userId);
		}
		return repository.save(proveedorSave);
	}
	
	public Proveedor saveAndFlushProveedor(Proveedor proveedor){
		return repository.saveAndFlush(proveedor);
	}
	
	public void flush(){
		repository.flush();
	}
	
	public Page<ProveedorView> find(String razonSocial, String cuit, String medioPago, int pageIndex, int rowsPerPage){
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Proveedor> query = builder.createQuery(Proveedor.class);
		CriteriaQuery<Long> queryCount = builder.createQuery(Long.class);
		Pageable pageable = constructPageSpecification(pageIndex, rowsPerPage);
		Root<Proveedor> fromProveedor = query.from(Proveedor.class);
		Root<Proveedor> fromProveedorCount = queryCount.from(Proveedor.class);
		fromProveedor = getRestrictionsProveedorFilter(razonSocial, cuit, medioPago, query,
				fromProveedor);
		fromProveedorCount = getRestrictionsProveedorFilter(razonSocial, cuit, medioPago, queryCount, fromProveedorCount);
		queryCount.multiselect(builder.count(fromProveedorCount.<String> get("id")));
		
		Long countTotal = em.createQuery(queryCount).getSingleResult();
		Double size =  countTotal.doubleValue();
		Double lastPage = Math.floor(rowsPerPage>0?(size/rowsPerPage):size);
				
		sortByNumeroDes(query, fromProveedor);
		List<Proveedor> result = em
				.createQuery(query.select(fromProveedor))
				.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
				.setMaxResults(pageable.getPageSize()).getResultList();
		List<ProveedorView> viewResult = new ArrayList<ProveedorView>();
		
		for (Proveedor prov : result) {
			viewResult.add(loadView(prov));
		}
		Page<ProveedorView> page = getPageViewConverter().convertFrom(
				new PageImpl<ProveedorView>(viewResult, pageable, size.longValue()));
		page.setTotalAmount((pageIndex) == lastPage.intValue()?ParseoService.alwaysTwoDecimal(countTotal.doubleValue()):null);
		return  page;
	}
	
	
	
	private ProveedorView loadView(Proveedor pro) {
		ProveedorView pv = new ProveedorView();
		pv.setObservaciones(pro.getObservaciones());
		pv.setRazonSocial(pro.getRazonSocial());
		pv.setCuit(pro.getCuit());
		pv.setContacto(pro.getContacto());
		pv.setDescuento(pro.getDescuento());
		pv.setDireccion(pro.getDireccion());
		pv.setEmail(pro.getEmail());
		pv.setId(pro.getId().toString());
		pv.setIibb(pro.getIibb());
		pv.setMedioPagoDefecto(pro.getMedioPagoDefecto().getId());
		List<Long> medios = new ArrayList<Long>();
		for(MedioPago mp: pro.getMesdiosPago()){
			medios.add(mp.getId());
		}
		pv.setMediosPago(medios);
		pv.setNumeroTelefono(pro.getNumeroTelefono());
		pv.setTipoTelefono(pro.getTipoTelefono() != null ? pro
				.getTipoTelefono().getId() : null);
		return pv;
	}
	
	private Proveedor updateProveedor(Proveedor pro, ProveedorView pv,
			Long userId) {
		pro.setUpdatedBy(userId);
		pro.setObservaciones(pv.getObservaciones());
		pro.setRazonSocial(pv.getRazonSocial());
		pro.setCuit(pv.getCuit());
		pro.setContacto(pv.getContacto());
		pro.setDescuento(pv.getDescuento());
		pro.setDireccion(pv.getDireccion());
		pro.setEmail(pv.getEmail());
		pro.setId(pv.getId() != null && !"".equals(pv.getId()) ? Long
				.parseLong(pv.getId()) : null);
		pro.setIibb(pv.getIibb());
		pro.setMedioPagoDefecto(pv.getMedioPagoDefecto() != null ? medioPagoRepository
				.findById(pv.getMedioPagoDefecto()) : null);
		pro.setMesdiosPago(pv.getMediosPago() != null
				&& !pv.getMediosPago().isEmpty() ? medioPagoRepository
				.findByIdIn(pv.getMediosPago()) : new ArrayList<MedioPago>());
		pro.setNumeroTelefono(pv.getNumeroTelefono());
		pro.setTipoTelefono(pv.getTipoTelefono() != null ? tipoTelefonoRepository
				.findById(pv.getTipoTelefono()) : null);
		return pro;
	}

	private Proveedor createProveedor(ProveedorView pv, Long userId) {
		Proveedor pro = updateProveedor(new Proveedor(), pv, userId);
		pro.setCreatedBy(userId);
		return pro;
	}
	
	private <X, T> void sortByNumeroDes(CriteriaQuery<X> query,
			Root<T> fromFactura) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		query.orderBy(builder.desc(fromFactura.get("id")));
	}
	

	private Pageable constructPageSpecification(int pageIndex, int rowsPerPage) {
		Pageable pageSpecification = new PageRequest(pageIndex-1, rowsPerPage);
		return pageSpecification;
	}
	
	
	
	private <T, X> Root<T> getRestrictionsProveedorFilter(String razonSocial, String cuit, String medioPago,
			CriteriaQuery<X> query, Root<T> fromProveedor) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		Long medioPagoLong = ParseoService.parseLong(medioPago);
		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(builder.like(fromProveedor.<String> get("razonSocial"),
				ParseoService.prepareString(razonSocial)));
		predicates.add(builder.like(fromProveedor.<String> get("cuit"),
				ParseoService.prepareString(cuit)));
		predicates.add(builder.equal(fromProveedor.<Boolean> get("deleted"),
				false));
		if (medioPagoLong != null) {
			Subquery<Proveedor> subQuery1 = query.subquery(Proveedor.class);
			Root<Proveedor> fromProveedorSubquery1 = subQuery1
					.from(Proveedor.class);
			Join<Proveedor, MedioPago> mpJoin = fromProveedorSubquery1.join(
					"medioPagoDefecto", JoinType.INNER);
			subQuery1.select(fromProveedorSubquery1);
			subQuery1.where(builder.equal(mpJoin.get("id"), medioPagoLong),
					builder.equal(fromProveedorSubquery1.get("id"),
							fromProveedor.get("id")));
			Subquery<Proveedor> subQuery2 = query.subquery(Proveedor.class);
			Root<Proveedor> fromProveedorSubquery2 = subQuery2
					.from(Proveedor.class);
			Join<Proveedor, MedioPago> mpsJoin = fromProveedorSubquery2.join(
					"mesdiosPago", JoinType.INNER);
			subQuery2.select(fromProveedorSubquery2);
			subQuery2.where(builder.equal(mpsJoin.get("id"), medioPagoLong),
					builder.equal(fromProveedorSubquery2.get("id"),
							fromProveedor.get("id")));

			predicates.add(builder.or(builder.exists(subQuery1),
					builder.exists(subQuery2)));
		}
		query.where(predicates.toArray(new Predicate[] {}));
		return fromProveedor;
	}

	@Override
	public ProveedorView findViewById(long id) {
		return  loadView(repository.findByIdAndDeleted(id, false));
	}

	@Override
	public List<TipoTelefono> getTiposTelefono() {
		return tipoTelefonoRepository.findAll();
	}

	@Override
	public List<MedioPago> getMediosPago() {
		return medioPagoRepository.findAll();
	}

	@Override
	public void deleteProveedor(long id) {
		Proveedor pro = repository.findById(id);
		pro.setDeleted(true);
		repository.save(pro);
	}

}
