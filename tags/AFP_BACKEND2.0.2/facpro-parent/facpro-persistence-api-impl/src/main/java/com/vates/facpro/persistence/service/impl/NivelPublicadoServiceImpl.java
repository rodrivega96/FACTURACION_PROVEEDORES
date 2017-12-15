package com.vates.facpro.persistence.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.NivelPublicado;
import com.vates.facpro.persistence.paging.PageConverter;
import com.vates.facpro.persistence.repository.NivelPublicadoRepository;
import com.vates.facpro.persistence.service.NivelPublicadoService;

/**
 * @author cparodi
 * 
 */
@Repository("nivelPublicadoService")
@Transactional(propagation = Propagation.NESTED)
public class NivelPublicadoServiceImpl extends
		AbstractBaseService<NivelPublicado, Long> implements
		NivelPublicadoService {

	@Inject
	private NivelPublicadoRepository repository;
	
//	@Inject
//	private FacturaAdmRepository facturaAdmRepository;
//
//	@Inject
//	private MailUtil mailUtil;
//
//	@Inject
//	private TemplateLoader templateLoader;
//	
//	@Inject
//	private UserRepository userRepository;

	@Override
	public NivelPublicado saveNivelPublicado(NivelPublicado nivelPublicado) {
		return repository.saveAndFlush(nivelPublicado);
	}

//	@Override
//	public void informarPendienteAutorizar(Nivel nivel)
//			throws MessagingException {
//		Map<String, String> map = new HashMap<String, String>();
//		FacturaAdm facturaAdm = facturaAdmRepository.findById(nivel
//				.getFactura().getFacturaAdm());
//		map.put("FIRMA", facturaAdm != null ? facturaAdm.getNroFactura(): "");
//		map.put("URL", templateLoader.getUrlBase());
//		map.put("MOTIVO", "Detalle");
//		map.put("DETALLE", "Pendiente de autorizar.");
//		map.put("FECHA",
//				nivel.getFactura().getVencimiento() != null ? ParseoService
//						.getFormattedDate(nivel.getFactura().getVencimiento())
//						: "");
//		map.put("PROVEEDOR",
//				facturaAdm!= null ? facturaAdm.getRazonSocial(): "");
//		map.put("DESC",
//				facturaAdm != null ? facturaAdm
//						.getDescripcion() : "");
//		mailUtil.sendMessage(userRepository.findMailById(nivel.getUsuarioId()),
//				"Autorizacion Factura",
//				templateLoader.getTemplate("pending", map), false);
//	}

	@Override
	public List<NivelPublicado> findFacturaByLast(boolean last) {
		return repository.findFacturaByLast(last);
	}

	@Override
	protected JpaRepository<NivelPublicado, Long> getRepository() {
		return repository;
	}

	@Override
	public NivelPublicado find(long id) {
		return repository.findOne(id);
	}

	@Override
	public NivelPublicado findNivelPublicadoByFacturaIdAndLast(long id) {
		return repository.findNivelPublicadoByFacturaIdAndLast(id, true);
	}

	@Override
	protected PageConverter<NivelPublicado> getPageConverter() {
		return null;
	}

}
