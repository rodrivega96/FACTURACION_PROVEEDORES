package com.vates.facpro.persistence.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.mail.MessagingException;

import org.apache.geronimo.javamail.transport.smtp.SMTPAddressFailedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vates.facpro.business.states.factura.Estados;
import com.vates.facpro.business.states.factura.EstadosNivel;
import com.vates.facpro.business.states.oc.EstadosOC;
import com.vates.facpro.persistence.domain.ArchivoHeader;
import com.vates.facpro.persistence.domain.Factura;
import com.vates.facpro.persistence.domain.FacturaAdm;
import com.vates.facpro.persistence.domain.FacturaView;
import com.vates.facpro.persistence.domain.Nivel;
import com.vates.facpro.persistence.domain.NivelPublicado;
import com.vates.facpro.persistence.domain.Notification;
import com.vates.facpro.persistence.domain.Orden;
import com.vates.facpro.persistence.mail.MailUtil;
import com.vates.facpro.persistence.mail.TemplateLoader;
import com.vates.facpro.persistence.repository.ArchivoHeaderRepository;
import com.vates.facpro.persistence.repository.ArchivoRepository;
import com.vates.facpro.persistence.repository.FacturaAdmRepository;
import com.vates.facpro.persistence.repository.FacturaRepository;
import com.vates.facpro.persistence.repository.NotificationRepository;
import com.vates.facpro.persistence.repository.OrdenRepository;
import com.vates.facpro.persistence.repository.UserRepository;
import com.vates.facpro.persistence.service.JobsService;
import com.vates.facpro.persistence.service.OrdenService;
import com.vates.facpro.persistence.util.ParseoService;

/**
 * @author
 * 
 */
@EnableScheduling
@Repository("JobsService")
@Transactional(propagation = Propagation.NESTED)
public class JobsServiceImpl implements JobsService {

	@Value("${time.to.delete.file}")
	private Integer timeDelete;
	
	@Inject
	private MailUtil mailUtil;

	@Inject
	private TemplateLoader templateLoader;

	@Inject
	private FacturaRepository facturaRepository;
	
	@Inject
	private FacturaAdmRepository facturaAdmRepository;
	
	@Inject
	private OrdenRepository ordenRepository;
	
	@Inject
	private OrdenService ordenService;
	
	@Inject
	private UserRepository userRepository;
	
	@Inject 
	private NotificationRepository notificationRepository;

	@Inject
	private ArchivoHeaderRepository archivoHeaderRepository;
	
	@Inject
	private ArchivoRepository archivoRepository; 
	
	@Override
	@Scheduled(cron = "${pending.cron.rate}")
	public void sendPendingNotification() {
		for (Factura fac : facturaRepository
				.findByEstadoAndTipoFacturaAndFacturaAdmIsNotNull(
						Estados.EN_AUTORIZACION, FacturaAdm.A_VENCER)) {
			if (!esVencida(fac.getVencimiento())) {
				sendMail(fac, userRepository.findMailById(fac.getNivel().getUsuarioId()),
						"Pendiente de su autorización", "Factura Pendiente",
						false);
			}
		}
		for (Factura fac : facturaRepository
				.findByEstadoAndTipoFacturaNotAndFacturaAdmIsNotNull(
						Estados.EN_AUTORIZACION, FacturaAdm.A_VENCER)) {
			sendMail(fac, userRepository.findMailById(fac.getNivel().getUsuarioId()),
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
				sendMail(fac, userRepository.findMailById(fac.getNivel().getUsuarioId()), "Vencida",
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
					sendMail(fac,userRepository.findMailById(fac.getNivel().getUsuarioId()),
							"Pendiente de su autorización",
							"Factura Pendiente", true);
				}
			}
		}
	}

	@Override
	@Scheduled(cron = "${daily.cron.rate}")
	public void sendDailyNotification() {
		Map<Long, List<FacturaView>> facturasPendientes = new HashMap<Long, List<FacturaView>>();
		String url = templateLoader.getUrlBase();
		for (Factura fac : facturaRepository
				.findByEstadoAndFacturaAdmIsNotNull(Estados.EN_AUTORIZACION)) {
			List<FacturaView> listaFacturas = facturasPendientes.get(fac
					.getNivel().getUsuarioId());
			if (listaFacturas == null) {
				listaFacturas = new ArrayList<FacturaView>();
				facturasPendientes.put(fac.getNivel().getUsuarioId(),
						listaFacturas);
			}
			FacturaView fv = new FacturaView();
			FacturaAdm facturaAdm = facturaAdmRepository.findById(fac.getFacturaAdm());
			fv.setNroFactura(fac.getFacturaAdm() != null ? facturaAdm
					.getNroFactura() : "");
			fv.setFechaVencimiento(fac.getVencimiento() != null ? ParseoService
					.getFormattedDate(fac.getVencimiento()) : "");
			fv.setDescripcion(fac.getFacturaAdm() != null ? facturaAdm
					.getDescripcion() : "");
			listaFacturas.add(fv);
		}
		for (Long key : facturasPendientes.keySet()) {
			sendMail(facturasPendientes.get(key), userRepository.findMailById(key), "Facturas Pendientes",
					url, "Facturas Pendientes", false);
		}
	}
	
	
	@Override
	@Scheduled(cron = "${authorization.cron.rate}")
	public void sendAuthorizationNotification() {
		synchronized ("JOBS_AUTHORIZATION") {
			List<Notification> toDelete = new ArrayList<Notification>();
			for (Notification notification : notificationRepository
					.findByType(Notification.TYPE_AUTHORIZATION)) {
				try {
					if (notification.getCc() != null) {
						String cc = notification.getCc();
						mailUtil.sendMessage(
								notification.getTo(),
								cc.substring(cc.indexOf("[") + 1,
										cc.indexOf("]")).split(","),
								notification.getSubject(),
								notification.getBody(),
								notification.getImportant());
					} else {
						mailUtil.sendMessage(notification.getTo(),
								notification.getSubject(),
								notification.getBody(),
								notification.getImportant());
					}
					toDelete.add(notification);

				} catch (MessagingException me){
					Throwable tw = me;
					boolean iterate = true;
					while (iterate) {
						if (tw.getCause() != null) {
							tw = tw.getCause();
						} else {
							iterate = false;
						}
					}
					//Si es un usuario invalido lo elimino.
					if (tw instanceof SMTPAddressFailedException
							&& "5.1.1 User unknown".equals(tw.getMessage())) {
						toDelete.add(notification);
					}
				}catch (Exception e) {
					
				}
			}
			if(!toDelete.isEmpty()){
				notificationRepository.delete(toDelete);
				notificationRepository.flush();
			}
		}
	}
	

	private void sendMail(List<FacturaView> facturas, String usuarioEmail,
			String descripcion, String url, String asunto, boolean important) {
		mailUtil.javaMailService();
		try {

			Map<String, List<? extends Object>> map = new HashMap<String, List<? extends Object>>();
			map.put("PENDIENTES", facturas);
			Map<String, String> map1 = new HashMap<String, String>();
			map1.put("TITULO", descripcion);
			map1.put("URL", url);
			mailUtil.sendMessage(usuarioEmail, asunto,
					templateLoader.getTemplate("pendingTable", map, map1),
					important);
		} catch (MessagingException e) {
			// Ver que hacer en caso de error
			e.printStackTrace();
		}
	}

	private void sendMail(Factura fac, String usuarioEmail, String descripcion,
			String asunto, boolean important) {
		mailUtil.javaMailService();
		try {
			Map<String, String> map = new HashMap<String, String>();
			FacturaAdm facturaAdm = facturaAdmRepository.findById(fac.getId());
			map.put("FIRMA", facturaAdm.getNroFactura());
			map.put("URL", templateLoader.getUrlBase());
			map.put("MOTIVO", "Detalle");
			map.put("DETALLE", descripcion);
			map.put("FECHA",
					fac.getVencimiento() != null ? ParseoService
							.getFormattedDate(fac.getVencimiento()) : "");
			map.put("DESC",
					fac.getFacturaAdm() != null ? facturaAdm.getDescripcion()
							: "");
			mailUtil.sendMessage(usuarioEmail, asunto,
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

	@Override
	@Scheduled(cron = "${delete.file.cron.rate}")
	public void deleteArchivo() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.HOUR, -timeDelete);
		List<ArchivoHeader> aHeaderList = archivoHeaderRepository
				.findByIdTmpIsNotNullAndCreatedAtLessThan(calendar.getTime());
		for (ArchivoHeader aHeader : aHeaderList) {
			archivoRepository.deleteArchivoByIdHeader(aHeader.getId());
			archivoHeaderRepository.delete(aHeader);
		}
	}
	
	@Override
	@Scheduled(cron = "${oc.close.cron.rate}")
	public void closeOcs() {
		List<Long> estados = new  ArrayList<Long>();
		estados.add(EstadosOC.VIGENTE);
		estados.add(EstadosOC.EXTENDIDA);
		//ordenRepository.findByEstadoInAndSaldoLessThanEqual(estados, 0.0D);
		for(Orden orden:ordenRepository.findByEstadoIn(estados)){
			if(canClose(orden)){
				ordenService.updateOrdenChangeState(orden, 0L, EstadosOC.CERRADA);
			}
		}
		
	}
	
	private boolean canClose(Orden orden){
		return true;
	}
}