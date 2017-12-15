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

import com.vates.facpro.persistence.domain.Factura;
import com.vates.facpro.persistence.domain.Nivel;
import com.vates.facpro.persistence.domain.NivelPublicado;
import com.vates.facpro.persistence.mail.MailUtil;
import com.vates.facpro.persistence.mail.TemplateLoader;
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
		AbstractBaseService<NivelPublicado, Long> implements NivelPublicadoService {
	

	@Inject
	private NivelPublicadoRepository repository;

	@Inject
	private MailUtil mailUtil;
	
	@Inject
	private TemplateLoader templateLoader;	

	@Override
	public NivelPublicado saveNivelPublicado(NivelPublicado nivelPublicado) {
		return repository.saveAndFlush(nivelPublicado);
	}
		
	@Override
	public void informarPendienteAutorizar(Nivel nivel) throws MessagingException{
		Map<String, String> map = new HashMap<String, String>();
		map.put("FIRMA", "La factura " + nivel.getFactura().getFacturaAdm().getNroFactura() 
				+ " en el sistema AFP, está pendiente de autorizar.");
		
		mailUtil.sendMessage(nivel.getUsuario().getMail(), "Autorizacion Factura",
				templateLoader.getTemplate("pending", map));
	}
	
	@Override
	public List<NivelPublicado> findFacturaByLast(boolean last){
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
	
//	@Override
//	public NivelPublicado findNivelPublicadoByPadreId(long id) {
//		return repository.findNivelPublicadoByPadreId(id);
//	}

	@Override
	protected PageConverter<NivelPublicado> getPageConverter() {
		return null;
	}

	
//	@Override
//	public void flush() {
//		repository.flush();		
//	}

	

}
