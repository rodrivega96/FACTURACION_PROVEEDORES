package com.vates.facpro.persistence.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.mail.MessagingException;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.business.states.Estados;
import com.vates.facpro.persistence.domain.Factura;
import com.vates.facpro.persistence.domain.FacturaAdm;
import com.vates.facpro.persistence.domain.User;
import com.vates.facpro.persistence.mail.MailUtil;
import com.vates.facpro.persistence.mail.TemplateLoader;
import com.vates.facpro.persistence.repository.FacturaRepository;
import com.vates.facpro.persistence.service.JobsService;

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
		for (Factura fac : facturaRepository.findAll()) {
			if (fac.getEstado().equals(Estados.EN_AUTORIZACION)) {
				if ((fac.getTipoFactura().equals(FacturaAdm.A_VENCER) && !esVencida(fac
						.getVencimiento()))
						|| !fac.getTipoFactura().equals(FacturaAdm.A_VENCER)) {
					sendMail(fac, fac.getNivel().getUsuario(),
							" esta pendiente de su autorización",
							"Factura Pendiente");
				}
			}
		}
	}

	@Override
	@Scheduled(cron = "${expired.cron.rate}")
	public void sendExpiredNotification() {
		for (Factura fac : facturaRepository.findAll()) {
			if (fac.getEstado().equals(Estados.EN_AUTORIZACION)) {
				if (fac.getTipoFactura().equals(FacturaAdm.A_VENCER)
						&& esVencida(fac.getVencimiento())) {
					sendMail(fac, fac.getNivel().getUsuario(), " esta vencida",
							"Factura Vencida");
				}
			}
		}
	}

	private void sendMail(Factura fac, User usuario, String descripcion,
			String asunto) {
		mailUtil.javaMailService();
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("FIRMA", "La Factura Nº "
					+ fac.getFacturaAdm().getNroFactura() + descripcion);
			mailUtil.sendMessage(usuario.getMail(), asunto,
					templateLoader.getTemplate("pending", map));
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
}