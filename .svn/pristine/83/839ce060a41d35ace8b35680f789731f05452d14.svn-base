package com.vates.facpro.persistence.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.mail.MessagingException;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.business.levels.AdministradorNivelesAut;
import com.vates.facpro.business.states.factura.Estados;
import com.vates.facpro.persistence.comparators.HistorialComparator;
import com.vates.facpro.persistence.domain.Factura;
import com.vates.facpro.persistence.domain.FacturaAdm;
import com.vates.facpro.persistence.domain.HistorialAutorizacion;
import com.vates.facpro.persistence.domain.Nivel;
import com.vates.facpro.persistence.domain.NivelPublicado;
import com.vates.facpro.persistence.domain.Notification;
import com.vates.facpro.persistence.domain.Role;
import com.vates.facpro.persistence.domain.User;
import com.vates.facpro.persistence.mail.TemplateLoader;
import com.vates.facpro.persistence.paging.PageConverter;
import com.vates.facpro.persistence.repository.FacturaAdmRepository;
import com.vates.facpro.persistence.repository.FacturaRepository;
import com.vates.facpro.persistence.repository.HistorialAutorizacionRepository;
import com.vates.facpro.persistence.repository.NivelPublicadoRepository;
import com.vates.facpro.persistence.repository.NivelRepository;
import com.vates.facpro.persistence.repository.NotificationRepository;
import com.vates.facpro.persistence.repository.UserRepository;
import com.vates.facpro.persistence.service.HistorialAutorizacionService;
import com.vates.facpro.persistence.util.ParseoService;

/**
 * @author
 * 
 */
@Repository("HistorialAutorizacionService")
@Transactional(propagation = Propagation.NESTED)
public class HistorialAutorizacionServiceImpl extends
		AbstractBaseService<HistorialAutorizacion, Long> implements
		HistorialAutorizacionService {

	@Inject
	private HistorialAutorizacionRepository repository;

	@Inject
	private UserRepository userRepository;

	@Inject
	private FacturaRepository facturaRepository;
	
	@Inject
	private NivelRepository nivelRepository;
	
	@Inject
	private FacturaAdmRepository facturaAdmRepository;
	
	@Inject
	private NotificationRepository notificationRepository;
	
	@Inject
	private NivelPublicadoRepository nivelPublicadoRepository;

	@Inject
	private TemplateLoader templateLoader;

	@Inject
	private PageConverter<HistorialAutorizacion> pageConverter;

	@Override
	protected JpaRepository<HistorialAutorizacion, Long> getRepository() {
		return repository;
	}

	@Override
	protected PageConverter<HistorialAutorizacion> getPageConverter() {
		return pageConverter;
	}

	@Override
	public List<HistorialAutorizacion> findByuserAndFactura(Long idFactura) {
		List<HistorialAutorizacion> historialList = new ArrayList<HistorialAutorizacion>();

		historialList = repository
				.findByFacturaIdOrderByUpdatedAtAsc(idFactura);
		Collections.sort(historialList, new HistorialComparator());

		return historialList;
	}

	@Override
	public void saveHistorial(Long idFactura, String motivo, Long estado, Long userId)
			throws MessagingException {
		Factura fac = facturaRepository.findById(idFactura);
		AdministradorNivelesAut adm = new AdministradorNivelesAut();
		HistorialAutorizacion his = adm.autorizar(fac, userId,
				nivelRepository.findNivelByFacturaId(fac.getId()),
				nivelPublicadoRepository.findNivelPublicadoByFacturaIdAndLast(fac.getId(), true), estado, motivo);
		if(his!=null){
			String asunto;
			String userEmail;
			Long estadoFac = null;
			if (fac.getNivel() == null) {
				if (estado.equals(HistorialAutorizacion.AUTORIZADA)) {
					estadoFac = Estados.AUTORIZADA;
					asunto = "Factura Autorizada: ";
				} else {
					estadoFac = Estados.OBSERVADA;
					asunto = "Factura Observada: ";
				}
				for (HistorialAutorizacion hisAut : repository
						.findByFacturaIdAndNivelEliminadoOrderByUpdatedAtAsc(
								fac.getId(), false)) {
					if (hisAut.getEstado().equals(HistorialAutorizacion.OBSERVADA)) {
						estadoFac = Estados.OBSERVADA;
						break;
					}
				}
				fac.setEstado(estadoFac);
				userEmail = userRepository.findMailById(fac.getWfUpdatedUser());
			} else {
				asunto = "Autorización Factura: ";
				userEmail = userRepository.findMailById(fac.getNivel()
						.getUsuarioId());
			}
			facturaRepository.save(fac);
			repository.save(his);
			sendAutEmail(asunto, fac, userEmail, estado, motivo);
			
		}
	}
	
	private void sendAutEmail(String asunto, Factura fac, String userEmail,
			Long estado, String motivo) throws MessagingException {
		FacturaAdm facturaAdm = facturaAdmRepository.findById(fac
				.getFacturaAdm());
		String cuerpo = "";
		String proveedor = facturaAdm.getRazonSocial() + " - $"
				+ facturaAdm.getGralNeto() + " (Neto: $"
				+ facturaAdm.getGralBruto() + ")";
		if (fac.getNivel() != null
				&& estado.equals(HistorialAutorizacion.OBSERVADA)) {
			asunto = "Autorización Factura con observación: " + proveedor;
			cuerpo = "Observada. El motivo: " + motivo;
			List<String> ccs = userRepository.findEmailsByRolesIdAndActive(Role.PAGO_PROVEEDOR, 1);
			sendMailCC(userEmail, ccs, fac, asunto, cuerpo, "Detalle",
					facturaAdm);
		} else {
			cuerpo = "Pendiente de autorizar.";
			asunto = asunto + proveedor;
			sendMail(userEmail, fac, asunto, cuerpo, facturaAdm);
		}
	}


	@Override
	public void autorizarAnterioresHistorial(Long idFactura, Long userId,
			String motivo, Long estado) throws MessagingException {
		Factura fac = facturaRepository.findById(idFactura);
		AdministradorNivelesAut adm = new AdministradorNivelesAut();
		List<HistorialAutorizacion> listHistorial = new ArrayList<HistorialAutorizacion>();
		List<Nivel> nivelesFactura = nivelRepository.findNivelByFacturaId(fac
				.getId());
		NivelPublicado nivelPublicadoLast = nivelPublicadoRepository
				.findNivelPublicadoByFacturaIdAndLast(fac.getId(), true);
		if(adm.autorizarSuperior(fac, userId, nivelesFactura, estado, motivo,
				nivelPublicadoLast, listHistorial)){
			repository.save(listHistorial);
			facturaRepository.save(fac);		
			String asunto;
			String userEmail;
			Long estadoFac = null;
			if (fac.getNivel() == null) {
				if (estado.equals(HistorialAutorizacion.AUTORIZADA)) {
					estadoFac = Estados.AUTORIZADA;
					asunto = "Factura Autorizada: ";
				} else {
					estadoFac = Estados.OBSERVADA;
					asunto = "Factura Observada: ";
				}
				fac.setEstado(estadoFac);
				userEmail = userRepository.findMailById(fac.getWfUpdatedUser());
			} else {
				asunto = "Autorizacion Factura: ";
				userEmail = userRepository.findMailById(fac.getNivel()
						.getUsuarioId());
			}
			sendAutEmail(asunto, fac, userEmail, estado, motivo);
		}
	}

	@Override
	public void rechazarAnterioresHistorial(Long idFactura, Long userId,
			String motivo) throws MessagingException {

		Factura fac = facturaRepository.findById(idFactura);
		FacturaAdm facturaAdm = facturaAdmRepository.findById(fac
				.getFacturaAdm());
		AdministradorNivelesAut adm = new AdministradorNivelesAut();
		List<HistorialAutorizacion> listHistorial = new ArrayList<HistorialAutorizacion>();
		List<Nivel> nivelesFactura = nivelRepository.findNivelByFacturaId(fac
				.getId());
		NivelPublicado nivelPublicadoLast = nivelPublicadoRepository
				.findNivelPublicadoByFacturaIdAndLast(fac.getId(), true);
		adm.rechazarSuperior(fac, userId, nivelesFactura, motivo,
				nivelPublicadoLast, listHistorial);
		repository.save(listHistorial);
		fac.setEstado(Estados.RECHAZADA);

		facturaRepository.save(fac);
		sendMailUserNotifyDismiss(idFactura, fac, motivo, facturaAdm);
	}

	@Override
	public void rechazarFacturaHistorial(Long idFac, String motivo, Long userId)
			throws MessagingException {
		Factura fac = facturaRepository.findById(idFac);
		AdministradorNivelesAut adm = new AdministradorNivelesAut();
		HistorialAutorizacion his = adm.rechazar(fac, userId, motivo);
		if(his!=null){
			fac.setEstado(Estados.RECHAZADA);
			repository.save(his);
			facturaRepository.save(fac);
			FacturaAdm facturaAdm = facturaAdmRepository.findById(fac
					.getFacturaAdm());
			sendMailUserNotifyDismiss(idFac, fac, motivo,facturaAdm);
		}
	}

	private void sendMailUserNotifyDismiss(Long idFac, Factura fac,
			String motivo, FacturaAdm facturaAdm) throws MessagingException {
		Set<String> ccs = new HashSet<String>();
		String asunto = "Factura rechazada: " + facturaAdm.getRazonSocial()
				+ " - $" + facturaAdm.getGralNeto() + " (Neto: $"
				+ facturaAdm.getGralBruto() + ")";
		for (User us : userRepository.findByRolesIdAndActive(
				Role.PAGO_PROVEEDOR, 1)) {
			ccs.add(us.getMail());
		}
		for (HistorialAutorizacion hisAut : repository
				.findByFacturaIdAndNivelEliminadoOrderByUpdatedAtAsc(idFac,
						false)) {

			List<Long> emails = new ArrayList<Long>();
			if (!hisAut.getEstado().equals(
					HistorialAutorizacion.RECHAZADA_SUPERIOR)) {
				emails.add(hisAut.getNivel().getUsuarioId());
			}
			if(!emails.isEmpty()){
				ccs.addAll(userRepository.findMailByIdIn(emails));
			}
		}
		sendMailCC(userRepository.findMailById(fac.getWfUpdatedUser()), new ArrayList<String>(ccs) ,
				fac, asunto, motivo, "Motivo rechazo", facturaAdm);
	}

	private void sendMail(String userEmail, Factura fac, String asunto,
			String cuerpo, FacturaAdm facturaAdm) throws MessagingException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("FIRMA", facturaAdm.getNroFactura());
		map.put("MOTIVO", "Detalle");
		map.put("DETALLE", cuerpo);
		map.put("DESC", facturaAdm != null ? facturaAdm
				.getDescripcion() : "");
		map.put("FECHA",
				fac.getVencimiento() != null ? ParseoService
						.getFormattedDate(fac.getVencimiento()) : "");
		map.put("PROVEEDOR",
				facturaAdm!= null ? facturaAdm.getRazonSocial(): "");
		map.put("URL", templateLoader.getUrlBase());
		saveMessage(userEmail, null, asunto,
				templateLoader.getTemplate("pending", map), false);
	}

	private void sendMailCC(String userEmail, List<String> ccs, Factura fac,
			String asunto, String cuerpo, String motivo, FacturaAdm facturaAdm)
			throws MessagingException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("FIRMA", facturaAdm != null?facturaAdm.getNroFactura():"");
		map.put("MOTIVO", motivo);
		map.put("DETALLE", cuerpo);
		map.put("FECHA",
				fac.getVencimiento() != null ? ParseoService
						.getFormattedDate(fac.getVencimiento()) : "");
		map.put("PROVEEDOR",
				facturaAdm!= null ? facturaAdm.getRazonSocial(): "");
		map.put("DESC", facturaAdm != null ? facturaAdm
				.getDescripcion() : "");
		map.put("URL", templateLoader.getUrlBase());
		saveMessage(userEmail, ccs, asunto,
				templateLoader.getTemplate("pending", map), false);
	}

	@Override
	public void informar(Long idFactura, Long userId) throws MessagingException {
		Map<String, String> map1 = new HashMap<String, String>();
		Map<String, FacturaAdm> map = new HashMap<String, FacturaAdm>();
		List<String> ccs = new ArrayList<String>();
		Factura fac = facturaRepository.findById(idFactura);
		FacturaAdm facturaAdm = facturaAdmRepository.findById(fac
				.getFacturaAdm());
		User user = userRepository.findOne(userId);
		List<HistorialAutorizacion> listHistorial = repository
				.findByFacturaIdAndNivelEliminadoOrderByUpdatedAtAsc(idFactura,
						false);
		String asunto = "Se solicita más información: "
				+ facturaAdm.getRazonSocial() + " - $"
				+ facturaAdm.getGralNeto() + " (Neto: $"
				+ facturaAdm.getGralBruto() + ")";
		List<Long> emails = new ArrayList<Long>();
		for (HistorialAutorizacion his : listHistorial) {
			emails.add(his.getNivel().getUsuarioId());
		}
		if (!emails.isEmpty()) {
			ccs.addAll(userRepository.findMailByIdIn(emails));
		} else {
			ccs.add(userRepository.findOne(fac.getWfUpdatedUser()).getMail());
		}
		map1.put("USUARIO", user.getName() + " " + user.getLastName());
		map.put("FacturaAdm", facturaAdm);
		map1.put("URL", templateLoader.getUrlBase());
		sendMailInformation(user, asunto, ccs, map, map1);
	}

	private void sendMailInformation(User user, String asunto,
			List<String> ccs, Map<String, FacturaAdm> map,
			Map<String, String> map1) throws MessagingException {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String c : ccs) {
			if (!first) {
				sb.append(", ");
			} else {
				first = false;
			}
			sb.append(c);
		}
		ccs.clear();
		ccs.add(user.getMail());
		saveMessage(sb.toString(), ccs, asunto,
				templateLoader.getTemplateByObject("information", map, map1),
		false);
	}
	
	private void saveMessage(String to, List<String> ccs, String subject,
			String body, boolean important) {
		Notification notification = new Notification();
		notification.setBody(body);
		if(ccs!=null && !ccs.isEmpty()){
			notification.setCc(ccs.toString());
		}
		notification.setType(Notification.TYPE_AUTHORIZATION);
		notification.setImportant(important);
		notification.setSubject(subject);
		notification.setTo(to);
		notificationRepository.saveAndFlush(notification);
	}

}