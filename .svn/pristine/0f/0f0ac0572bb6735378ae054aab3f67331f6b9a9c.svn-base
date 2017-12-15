package com.vates.facpro.persistence.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.mail.MessagingException;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.business.states.Estados;
import com.vates.facpro.business.states.EstadosNivel;
import com.vates.facpro.persistence.domain.Factura;
import com.vates.facpro.persistence.domain.FacturaAdm;
import com.vates.facpro.persistence.domain.FacturaView;
import com.vates.facpro.persistence.domain.Nivel;
import com.vates.facpro.persistence.domain.NivelPublicado;
import com.vates.facpro.persistence.domain.User;
import com.vates.facpro.persistence.mail.MailUtil;
import com.vates.facpro.persistence.mail.TemplateLoader;
import com.vates.facpro.persistence.repository.FacturaRepository;
import com.vates.facpro.persistence.service.JobsService;
import com.vates.facpro.persistence.util.ParseoService;

/**
 * @author
 * 
 */
@EnableScheduling
@Repository("JobsService")
@Transactional(propagation = Propagation.NESTED)
public class JobsServiceImpl implements JobsService {

	@Inject
	private MailUtil mailUtil;

	@Inject
	private TemplateLoader templateLoader;

	@Inject
	private FacturaRepository facturaRepository;

	@Override
	@Scheduled(cron = "${pending.cron.rate}")
	public void sendPendingNotification() {
		for (Factura fac : facturaRepository
				.findByEstadoAndTipoFacturaAndFacturaAdmIsNotNull(
						Estados.EN_AUTORIZACION, FacturaAdm.A_VENCER)) {
			if (!esVencida(fac.getVencimiento())) {
				sendMail(fac, fac.getNivel().getUsuario(),
						"Pendiente de su autorización", "Factura Pendiente",
						false);
			}
		}
		for (Factura fac : facturaRepository
				.findByEstadoAndTipoFacturaNotAndFacturaAdmIsNotNull(
						Estados.EN_AUTORIZACION, FacturaAdm.A_VENCER)) {
			sendMail(fac, fac.getNivel().getUsuario(),
					"Pendiente de su autorización", "Factura Pendiente", false);
		}
	}

	@Override
	@Scheduled(cron = "${expired.cron.rate}")
	public void sendExpiredNotification() {
		for (Factura fac : facturaRepository
				.findByEstadoAndTipoFacturaAndFacturaAdmIsNotNull(
						Estados.EN_AUTORIZACION, FacturaAdm.A_VENCER)) {
			if (esVencida(fac.getVencimiento())) {
				sendMail(fac, fac.getNivel().getUsuario(), "Vencida",
						"Factura Vencida", true);
			}
		}
	}

	@Override
	@Scheduled(cron = "${before.expire.cron.rate}")
	public void sendBeforeExpireNotification() {
		Integer diasAntes = getDiasNumerico(
				templateLoader.getDiasAntesVencimiento(), 2);
		for (Factura fac : facturaRepository
				.findByEstadoAndTipoFacturaAndFacturaAdmIsNotNull(
						Estados.EN_AUTORIZACION, FacturaAdm.A_VENCER)) {
			if (!esVencida(fac.getVencimiento())) {
				fac.getVencimiento();
				Long diasAntesTotal = (long) (getNivelesPendientes(fac).size() * diasAntes);
				if (venceDiasAntes(fac.getVencimiento(), diasAntesTotal)) {
					sendMail(fac, fac.getNivel().getUsuario(),
							"Pendiente de su autorización",
							"Factura Pendiente", true);
				}
			}
		}
	}

	@Override
	@Scheduled(cron = "${daily.cron.rate}")
	public void sendDailyNotification() {
		Map<User, List<FacturaView>> facturasPendientes = new HashMap<User, List<FacturaView>>();
		String url = templateLoader.getUrlBase();
		for (Factura fac : facturaRepository
				.findByEstadoAndFacturaAdmIsNotNull(Estados.EN_AUTORIZACION)) {
			List<FacturaView> listaFacturas = facturasPendientes.get(fac
					.getNivel().getUsuario());
			if (listaFacturas == null) {
				listaFacturas = new ArrayList<FacturaView>();
				facturasPendientes.put(fac.getNivel().getUsuario(),
						listaFacturas);
			}
			FacturaView fv = new FacturaView();
			fv.setNroFactura(fac.getFacturaAdm() != null ? fac.getFacturaAdm()
					.getNroFactura() : "");
			fv.setFechaVencimiento(fac.getVencimiento() != null ? ParseoService
					.getFormattedDate(fac.getVencimiento()) : "");
			fv.setDescripcion(fac.getFacturaAdm() != null ? fac.getFacturaAdm()
					.getDescripcion() : "");
			listaFacturas.add(fv);
		}
		for (User key : facturasPendientes.keySet()) {
			sendMail(facturasPendientes.get(key), key, "Facturas Pendientes",
					url, "Facturas Pendientes", false);
		}
	}

	private void sendMail(List<FacturaView> facturas, User usuario,
			String descripcion, String url, String asunto, boolean important) {
		mailUtil.javaMailService();
		try {

			Map<String, List<? extends Object>> map = new HashMap<String, List<? extends Object>>();
			map.put("PENDIENTES", facturas);
			Map<String, String> map1 = new HashMap<String, String>();
			map1.put("TITULO", descripcion);
			map1.put("URL", url);
			mailUtil.sendMessage(usuario.getMail(), asunto,
					templateLoader.getTemplate("pendingTable", map, map1),
					important);
		} catch (MessagingException e) {
			// Ver que hacer en caso de error
			e.printStackTrace();
		}
	}

	private void sendMail(Factura fac, User usuario, String descripcion,
			String asunto, boolean important) {
		mailUtil.javaMailService();
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("FIRMA", fac.getFacturaAdm().getNroFactura());
			map.put("URL", templateLoader.getUrlBase());
			map.put("MOTIVO", "Detalle");
			map.put("DETALLE", descripcion);
			map.put("FECHA",
					fac.getVencimiento() != null ? ParseoService
							.getFormattedDate(fac.getVencimiento()) : "");
			map.put("DESC", fac.getFacturaAdm() != null ? fac.getFacturaAdm()
					.getDescripcion() : "");
			mailUtil.sendMessage(usuario.getMail(), asunto,
					templateLoader.getTemplate("pending", map), important);
		} catch (MessagingException e) {
			// Ver que hacer en caso de error
			e.printStackTrace();
		}
	}

	private boolean esVencida(Date fechaVencimietno) {
		boolean result = true;
		Date now = new Date();
		Long diff = Math.abs((now.getTime() - fechaVencimietno.getTime())
				/ (1000 * 60 * 60 * 24));
		if (now.compareTo(fechaVencimietno) < 0 && diff < 6) {
			result = false;
		}
		return result;
	}

	private boolean venceDiasAntes(Date fechaVencimietno, Long dias) {
		Date now = new Date();
		// Calculo la diferencia en días.
		Long diff = Math.abs((now.getTime() - fechaVencimietno.getTime())
				/ (1000 * 60 * 60 * 24));
		// Si la fecha actual esta antes que la fecha de vencimiento
		// y la diferencia entre las fechas es igual a la cantidad establecida
		// en dias.
		if (now.compareTo(fechaVencimietno) < 0 && diff == dias) {
			return true;
		}
		return false;
	}

	private List<Nivel> getNivelesPendientes(Factura factura) {
		Set<Nivel> auxNivel = new HashSet<Nivel>();
		for (NivelPublicado nvlPublic : factura.getNivelesPublicados()) {
			if (nvlPublic.getLast()) {
				for (Nivel nvl : nvlPublic.getNiveles()) {
					if (nvl.getEstado() != EstadosNivel.AUTORIZADO
							&& nvl.getEstado() != EstadosNivel.RECHAZADO
							&& nvl.getEstado() != EstadosNivel.OBSERVADO) {
						auxNivel.add(nvl);
					}
				}

				break;
			}
		}
		return new ArrayList<Nivel>(auxNivel);
	}

	private Integer getDiasNumerico(String dias, Integer defaultValue) {
		Integer diasAntes;
		try {
			diasAntes = Integer.parseInt(dias);
		} catch (NumberFormatException ex) {
			return defaultValue;
		}
		return diasAntes;
	}
}