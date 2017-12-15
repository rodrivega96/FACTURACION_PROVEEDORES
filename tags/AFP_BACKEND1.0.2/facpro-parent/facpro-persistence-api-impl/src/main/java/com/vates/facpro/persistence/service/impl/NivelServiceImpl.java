package com.vates.facpro.persistence.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.mail.MessagingException;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.persistence.domain.FacturaAdm;
import com.vates.facpro.persistence.domain.Nivel;
import com.vates.facpro.persistence.mail.MailUtil;
import com.vates.facpro.persistence.mail.TemplateLoader;
import com.vates.facpro.persistence.paging.PageConverter;
import com.vates.facpro.persistence.repository.FacturaAdmRepository;
import com.vates.facpro.persistence.repository.NivelRepository;
import com.vates.facpro.persistence.repository.UserRepository;
import com.vates.facpro.persistence.service.NivelService;
import com.vates.facpro.persistence.util.ParseoService;

/**
 * @author cparodi
 * 
 */
@Repository("nivelService")
@Transactional(propagation = Propagation.NESTED)
public class NivelServiceImpl extends AbstractBaseService<Nivel, Long>
		implements NivelService {

	@Inject
	private NivelRepository repository;

	@Inject
	private MailUtil mailUtil;

	@Inject
	private TemplateLoader templateLoader;
	
	@Inject
	private UserRepository userRepository;
	
	@Inject
	private FacturaAdmRepository facturaAdmRepository;

	@Override
	public Nivel saveNivel(Nivel nivel) {
		return repository.saveAndFlush(nivel);
	}

	@Override
	public void saveNiveles(List<Nivel> niveles) throws MessagingException {
		for (Nivel n : niveles) {
			n = saveNivel(n);
		}
	}

	@Override
	public void deleteByFacturaId(Long id) {
		List<Nivel> niveles = repository.findNivelByFacturaId(id);
		for (Nivel n : niveles) {
			repository.delete(n);
		}
	}

	@Override
	public void informarPendienteAutorizar(Nivel nivel)
			throws MessagingException {
		Map<String, String> map = new HashMap<String, String>();
		FacturaAdm facturaAdm = facturaAdmRepository.findById(nivel
				.getFactura().getFacturaAdm());
		map.put("FIRMA", facturaAdm.getNroFactura());
		map.put("URL", templateLoader.getUrlBase());
		map.put("MOTIVO", "Detalle");
		map.put("DETALLE", "Pendiente de autorizar.");
		map.put("FECHA",
				nivel.getFactura().getVencimiento() != null ? ParseoService
						.getFormattedDate(nivel.getFactura().getVencimiento())
						: "");
		map.put("DESC",
				nivel.getFactura().getFacturaAdm() != null ? facturaAdm
						.getDescripcion() : "");
        String subject = "Autorizacion Factura: "+facturaAdm.getRazonSocial()+" - $"+facturaAdm.getGralNeto()
        		+" (Neto: $"+facturaAdm.getGralBruto()+")";
		mailUtil.sendMessage(userRepository.findMailById(nivel.getUsuarioId()),
				subject,
				templateLoader.getTemplate("pending", map), false);
	}

	@Override
	protected JpaRepository<Nivel, Long> getRepository() {
		return repository;
	}

	@Override
	public Nivel find(long id) {
		return repository.findOne(id);
	}

	@Override
	public List<Nivel> findNivelByFacturaId(long id) {
		return repository.findNivelByFacturaId(id);
	}

	@Override
	public List<Nivel> findNivelByFacturaIdAndEliminado(long id,
			boolean eliminado) {
		return repository.findNivelByFacturaIdAndEliminado(id, eliminado);
	}

	@Override
	protected PageConverter<Nivel> getPageConverter() {
		return null;
	}

	@Override
	public void flush() {
		repository.flush();
	}

}
