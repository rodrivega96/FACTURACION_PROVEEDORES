package com.vates.facpro.persistence.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.mail.MessagingException;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.business.levels.AdministradorNivelesAut;
import com.vates.facpro.business.states.Estados;
import com.vates.facpro.business.states.EstadosNivel;
import com.vates.facpro.persistence.comparators.HistorialComparator;
import com.vates.facpro.persistence.domain.Factura;
import com.vates.facpro.persistence.domain.FacturaAdm;
import com.vates.facpro.persistence.domain.HistorialAutorizacion;
import com.vates.facpro.persistence.domain.Nivel;
import com.vates.facpro.persistence.domain.Role;
import com.vates.facpro.persistence.domain.User;
import com.vates.facpro.persistence.mail.MailUtil;
import com.vates.facpro.persistence.mail.TemplateLoader;
import com.vates.facpro.persistence.paging.PageConverter;
import com.vates.facpro.persistence.repository.FacturaAdmRepository;
import com.vates.facpro.persistence.repository.FacturaRepository;
import com.vates.facpro.persistence.repository.HistorialAutorizacionRepository;
import com.vates.facpro.persistence.repository.NivelRepository;
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
	private MailUtil mailUtil;

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
	public List<HistorialAutorizacion> findByuserAndFactura(Long idUsuario,
			Long idFactura) {
		List<HistorialAutorizacion> historialList = new ArrayList<HistorialAutorizacion>();

		historialList = repository
				.findByFacturaIdOrderByUpdatedAtAsc(idFactura);
		Collections.sort(historialList, new HistorialComparator());

		return historialList;
	}

	@Override
	public void saveHistorial(Long idFactura, String motivo, Long estado)
			throws MessagingException {
		Factura fac = facturaRepository.findById(idFactura);
		saveHistorial(estado, motivo, fac);
		AdministradorNivelesAut adm = new AdministradorNivelesAut();
		adm.autorizar(fac, null,
				nivelRepository.findNivelByFacturaId(fac.getId()));

		FacturaAdm facturaAdm = facturaAdmRepository.findById(fac
				.getFacturaAdm());

		String asunto;
		String proveedor = facturaAdm.getRazonSocial() + " - $"
				+ facturaAdm.getGralNeto() + " (Neto: $"
				+ facturaAdm.getGralBruto() + ")";
		String userEmail;
		Long estadoFac = null;
		String cuerpo = "Pendiente de autorizar.";

		if (fac.getNivel() == null) {
			if (estado.equals(HistorialAutorizacion.AUTORIZADA)) {
				estadoFac = Estados.AUTORIZADA;
				asunto = "Factura Autorizada: " + proveedor;
			} else {
				estadoFac = Estados.OBSERVADA;
				asunto = "Factura Observada: " + proveedor;
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
			asunto = "Autorización Factura: " + proveedor;
			userEmail = userRepository.findMailById(fac.getNivel()
					.getUsuarioId());
		}
		facturaRepository.save(fac);

		if (fac.getNivel() != null
				&& estado.equals(HistorialAutorizacion.OBSERVADA)) {
			asunto = "Autorización Factura con observación: " + proveedor;
			cuerpo = "Observada. El motivo: " + motivo;
			List<String> ccs = new ArrayList<String>();
			for (User us : userRepository.findByRolesIdAndActive(
					Role.PAGO_PROVEEDOR, 1)) {
				ccs.add(us.getMail());
			}
			sendMailCC(userEmail, ccs, fac, asunto, cuerpo, "Detalle");
		} else {
			sendMail(userEmail, fac, asunto, cuerpo);
		}
	}

	private void saveHistorial(Long estado, String motivo, Factura fac) {
		HistorialAutorizacion his = new HistorialAutorizacion();
		his.setEstado(estado);
		his.setDescripcion(motivo);
		his.setFactura(fac);
		his.setNivel(fac.getNivel());
		repository.save(his);
	}

	@Override
	public void autorizarAnterioresHistorial(Long idFactura, Long userId,
			String motivo, Long estado) throws MessagingException {
		Factura fac = facturaRepository.findById(idFactura);
		AdministradorNivelesAut adm = new AdministradorNivelesAut();
		List<HistorialAutorizacion> listHistorial = new ArrayList<HistorialAutorizacion>();
		List<Nivel> nivelesFactura = nivelRepository.findNivelByFacturaId(fac
				.getId());
		List<Nivel> listNvl = adm.getNivelesRestantes(fac, nivelesFactura);
		for (Nivel nvl : adm.getNivelesRestantes(fac, nivelesFactura)) {
			HistorialAutorizacion his = new HistorialAutorizacion();
			his.setFactura(fac);
			his.setNivel(nvl);
			adm.autorizarSuperior(fac, null, listNvl);
			if (!nvl.getUsuarioId().equals(userId)) {
				his.setEstado(estado.equals(HistorialAutorizacion.AUTORIZADA) ? HistorialAutorizacion.AUTORIZADA_SUPERIOR
						: HistorialAutorizacion.OBSERVADA_SUPERIOR);
				his.setDescripcion(estado
						.equals(HistorialAutorizacion.AUTORIZADA) ? ""
						: HistorialAutorizacion.OBSERVADA_SUPER);
				listHistorial.add(his);
			} else {
				his.setEstado(estado);
				his.setDescripcion(motivo);
				listHistorial.add(his);
				break;
			}
			listNvl.remove(nvl);
		}
		repository.save(listHistorial);
		facturaRepository.save(fac);

		FacturaAdm facturaAdm = facturaAdmRepository.findById(fac
				.getFacturaAdm());

		String asunto;
		String proveedor = facturaAdm.getRazonSocial() + " - $"
				+ facturaAdm.getGralNeto() + " (Neto: $"
				+ facturaAdm.getGralBruto() + ")";
		String userEmail;
		Long estadoFac = null;
		String cuerpo = "Pendiente de autorizar.";

		if (fac.getNivel() == null) {
			if (estado.equals(HistorialAutorizacion.AUTORIZADA)) {
				estadoFac = Estados.AUTORIZADA;
				asunto = "Factura Autorizada: " + proveedor;
			} else {
				estadoFac = Estados.OBSERVADA;
				asunto = "Factura Observada: " + proveedor;
			}
			fac.setEstado(estadoFac);
			userEmail = userRepository.findMailById(fac.getWfUpdatedUser());
		} else {
			asunto = "Autorizacion Factura: " + proveedor;
			userEmail = userRepository.findMailById(fac.getNivel()
					.getUsuarioId());
		}

		if (fac.getNivel() != null
				&& estado.equals(HistorialAutorizacion.OBSERVADA)) {
			asunto = "Autorización Factura con observación: " + proveedor;
			cuerpo = "Observada. El motivo: " + motivo;
			List<String> ccs = new ArrayList<String>();
			for (User us : userRepository.findByRolesIdAndActive(
					Role.PAGO_PROVEEDOR, 1)) {
				ccs.add(us.getMail());
			}
			sendMailCC(userEmail, ccs, fac, asunto, cuerpo, "Detalle");
		} else {
			sendMail(userEmail, fac, asunto, cuerpo);
		}

	}

	@Override
	public void rechazarAnterioresHistorial(Long idFactura, Long userId,
			String motivo) throws MessagingException {

		Factura fac = facturaRepository.findById(idFactura);
		AdministradorNivelesAut adm = new AdministradorNivelesAut();
		List<HistorialAutorizacion> listHistorial = new ArrayList<HistorialAutorizacion>();
		List<Nivel> nivelesFactura = nivelRepository.findNivelByFacturaId(fac.getId());
		List<Nivel> listNvl = adm.getNivelesRestantes(fac,nivelesFactura);
		for (Nivel nvl : adm.getNivelesRestantes(fac, nivelesFactura)) {
			HistorialAutorizacion his = new HistorialAutorizacion();
			fac.getNivel().setEstado(EstadosNivel.RECHAZADO);
			his.setFactura(fac);
			his.setNivel(fac.getNivel());
			adm.rechazar(fac, null, listNvl);
			if (!nvl.getUsuarioId().equals(userId)) {
				his.setEstado(HistorialAutorizacion.RECHAZADA_SUPERIOR);
				his.setDescripcion(HistorialAutorizacion.RECHAZADA_SUPER);
				listHistorial.add(his);
			} else {
				his.setEstado(HistorialAutorizacion.RECHAZADA);
				his.setDescripcion(motivo);
				listHistorial.add(his);
				break;
			}
			listNvl.remove(nvl);
		}

		repository.save(listHistorial);
		fac.setEstado(Estados.RECHAZADA);

		facturaRepository.save(fac);
		sendMailUserNotifyDismiss(idFactura, fac, motivo);
	}

	@Override
	public void rechazarFacturaHistorial(Long idFac, String motivo)
			throws MessagingException {
		Factura fac = facturaRepository.findById(idFac);
		HistorialAutorizacion his = new HistorialAutorizacion();
		his.setEstado(HistorialAutorizacion.RECHAZADA);
		his.setDescripcion(motivo);
		his.setFactura(fac);
		fac.getNivel().setEstado(EstadosNivel.RECHAZADO);
		his.setNivel(fac.getNivel());
		repository.save(his);

		fac.setEstado(Estados.RECHAZADA);
		fac.setNivel(null);

		facturaRepository.save(fac);
		sendMailUserNotifyDismiss(idFac, fac, motivo);
	}

	private void sendMailUserNotifyDismiss(Long idFac, Factura fac,
			String motivo) throws MessagingException {
		List<String> ccs = new ArrayList<String>();
		FacturaAdm facturaAdm = facturaAdmRepository.findById(fac
				.getFacturaAdm());
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
				// ccs.add(hisAut.getNivel().getUsuario().getMail());
			}
			ccs.addAll(userRepository.findMailByIdIn(emails));

		}
		sendMailCC(userRepository.findMailById(fac.getWfUpdatedUser()), ccs,
				fac, asunto, motivo, "Motivo rechazo");
	}

	private void sendMail(String userEmail, Factura fac, String asunto,
			String cuerpo) throws MessagingException {
		Map<String, String> map = new HashMap<String, String>();
		FacturaAdm facturaAdm = facturaAdmRepository.findById(fac.getFacturaAdm());
		map.put("FIRMA", facturaAdm.getNroFactura());
		map.put("MOTIVO", "Detalle");
		map.put("DETALLE", cuerpo);
		map.put("DESC", fac.getFacturaAdm() != null ? facturaAdm
				.getDescripcion() : "");
		map.put("FECHA",
				fac.getVencimiento() != null ? ParseoService
						.getFormattedDate(fac.getVencimiento()) : "");
		map.put("URL", templateLoader.getUrlBase());
		mailUtil.sendMessage(userEmail, asunto,
				templateLoader.getTemplate("pending", map), false);
	}

	private void sendMailCC(String userEmail, List<String> ccs, Factura fac,
			String asunto, String cuerpo, String motivo)
			throws MessagingException {
		Map<String, String> map = new HashMap<String, String>();
		FacturaAdm facturaAdm = facturaAdmRepository.findById(fac.getFacturaAdm());
		map.put("FIRMA", facturaAdm.getNroFactura());
		map.put("MOTIVO", motivo);
		map.put("DETALLE", cuerpo);
		map.put("FECHA",
				fac.getVencimiento() != null ? ParseoService
						.getFormattedDate(fac.getVencimiento()) : "");
		map.put("DESC", fac.getFacturaAdm() != null ? facturaAdm
				.getDescripcion() : "");
		map.put("URL", templateLoader.getUrlBase());
		mailUtil.sendMessage(userEmail, ccs, asunto,
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
			// ccs.add(his.getNivel().getUsuario().getMail());
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
			List<String> ccs, Map<String, FacturaAdm> map, Map<String, String> map1)
			throws MessagingException {
		mailUtil.sendMessage(user.getMail(), ccs, asunto,
				templateLoader.getTemplateByObject("information", map, map1),
				false);
	}

}