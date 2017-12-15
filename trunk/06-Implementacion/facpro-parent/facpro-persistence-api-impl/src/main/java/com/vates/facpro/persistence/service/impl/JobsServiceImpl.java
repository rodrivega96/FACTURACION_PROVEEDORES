package com.vates.facpro.persistence.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.vates.facpro.business.states.oc.AdministradorEstadosOC;
import com.vates.facpro.persistence.domain.ArchivoHeader;
import com.vates.facpro.persistence.domain.Factura;
import com.vates.facpro.persistence.domain.FacturaAdm;
import com.vates.facpro.persistence.domain.FacturaView;
import com.vates.facpro.persistence.domain.HistorialOC;
import com.vates.facpro.persistence.domain.Notification;
import com.vates.facpro.persistence.domain.Orden;
import com.vates.facpro.persistence.mail.MailUtil;
import com.vates.facpro.persistence.mail.TemplateLoader;
import com.vates.facpro.persistence.repository.ArchivoHeaderRepository;
import com.vates.facpro.persistence.repository.ArchivoRepository;
import com.vates.facpro.persistence.repository.FacturaAdmRepository;
import com.vates.facpro.persistence.repository.FacturaRepository;
import com.vates.facpro.persistence.repository.HistorialRepository;
import com.vates.facpro.persistence.repository.NivelPublicadoRepository;
import com.vates.facpro.persistence.repository.NotificationRepository;
import com.vates.facpro.persistence.repository.OrdenRepository;
import com.vates.facpro.persistence.repository.UserRepository;
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
	
	private static Long MILIS_TO_DAY =  1000L * 60L * 60L * 24L;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

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
	private NivelPublicadoRepository nivelPublicadoRepository;	
	
	@Inject
	private OrdenRepository ordenRepository;
	
	@Inject
	private HistorialRepository historialRepository;
	
	@Inject
	private UserRepository userRepository;
	
	@Inject 
	private NotificationRepository notificationRepository;

	@Inject
	private ArchivoHeaderRepository archivoHeaderRepository;
	
	@Inject
	private ArchivoRepository archivoRepository; 

	@Override
	@Scheduled(cron = "${expired.cron.rate}")
	public void sendExpiredNotification() {
		Map<Long, List<FacturaView>> mapVencidas = new HashMap<Long, List<FacturaView>>();
		List<Long> facturaAdmLongList = new ArrayList<Long>();
		for (Factura fac : facturaRepository
				.findByEstadoAndTipoFacturaAndFacturaAdmIsNotNull(
						Estados.EN_AUTORIZACION, FacturaAdm.A_VENCER)) {
			if (esVencida(fac.getVencimiento())) {
				createMapByUser(mapVencidas, fac, facturaAdmLongList);
			}
		}
		if (!mapVencidas.isEmpty()) {
			String url = templateLoader.getUrlBase();
			List<FacturaAdm> facturaAdmList = facturaAdmRepository
					.findByIdIn(facturaAdmLongList);
			for (Long keyUser : mapVencidas.keySet()) {
				createMailByUser(mapVencidas, facturaAdmLongList,
						facturaAdmList, keyUser);
				sendExpiredMail(userRepository.findMailById(keyUser),
						"Facturas Vencidas", url, "Facturas Vencidas",
						mapVencidas.get(keyUser), false);
			}
		}
	}

	@Override
	@Scheduled(cron = "${before.expire.cron.rate}")
	public void sendBeforeExpireNotification() {
		Integer diasAntes = getDiasNumerico(
				templateLoader.getDiasAntesVencimiento(), 2);
		Map<Long, List<FacturaView>> facturasProximas = new HashMap<Long, List<FacturaView>>();
		List<Long> facturaAdmLongList = new ArrayList<Long>();
		for (Factura fac : facturaRepository
				.findByEstadoAndTipoFacturaAndFacturaAdmIsNotNull(
						Estados.EN_AUTORIZACION, FacturaAdm.A_VENCER)) {
			if (!esVencida(fac.getVencimiento())) {
				Long diasAntesTotal = (long) (getNivelesPendientesCount(fac) * diasAntes);
				if (venceDiasAntes(fac.getVencimiento(), diasAntesTotal)) {
					createMapByUser(facturasProximas, fac, facturaAdmLongList);
				}
			}
		}
		if (!facturasProximas.isEmpty()) {
			String url = templateLoader.getUrlBase();
			List<FacturaAdm> facturaAdmList = facturaAdmRepository
					.findByIdIn(facturaAdmLongList);
			for (Long keyUser : facturasProximas.keySet()) {
				createMailByUser(facturasProximas, facturaAdmLongList,
						facturaAdmList, keyUser);
				sendNextExpiredMail(userRepository.findMailById(keyUser),
						"Facturas Proximas a Vencer", url,
						"Facturas Proximas a Vencer",
						facturasProximas.get(keyUser), false);
			}
		}
	}

	@Override
	@Scheduled(cron = "${daily.cron.rate}")
	public void sendDailyNotification() {
		Map<Long, List<FacturaView>> facturasPendientes = new HashMap<Long, List<FacturaView>>();
		List<Long> facturaAdmLongList = new ArrayList<Long>();
		for (Factura fac : facturaRepository
				.findByEstadoAndFacturaAdmIsNotNull(Estados.EN_AUTORIZACION)) {
			createMapByUser(facturasPendientes, fac, facturaAdmLongList);
		}
		if (!facturasPendientes.isEmpty()) {
			String url = templateLoader.getUrlBase();
			List<FacturaAdm> facturaAdmList = facturaAdmRepository
					.findByIdIn(facturaAdmLongList);
			for (Long keyUser : facturasPendientes.keySet()) {
				createMailByUser(facturasPendientes, facturaAdmLongList,
						facturaAdmList, keyUser);
				sendPendingMail(userRepository.findMailById(keyUser),
						"Facturas Pendientes", url, "Facturas Pendientes",
						facturasPendientes.get(keyUser), false);
			}
		}
	}
	
	private void createMailByUser(Map<Long, List<FacturaView>> facturasMap,
			List<Long> facturaAdmLongList, List<FacturaAdm> facturaAdmList,
			Long keyUser) {
		for (FacturaView fv : facturasMap.get(keyUser)) {
			FacturaAdm facturaAdm = getFacturaAdm(facturaAdmList,
					fv.getIdFacturaAdm());
			if (facturaAdm != null) {
				facturaAdmList.remove(facturaAdm);
			}
			fv.setNroFactura(facturaAdm != null ? facturaAdm.getNroFactura()
					: "");
			fv.setDescripcion(facturaAdm != null ? facturaAdm.getDescripcion()
					: "");
			fv.setRazonSocial(facturaAdm != null ? facturaAdm.getRazonSocial()
					: "");
		}
	}
	
	private void createMapByUser(Map<Long, List<FacturaView>> mapFacturas,
			Factura fac, List<Long> facturaAdmLongList) {
		List<FacturaView> listaFacturas = mapFacturas.get(fac.getNivel()
				.getUsuarioId());
		if (listaFacturas == null) {
			listaFacturas = new ArrayList<FacturaView>();
			mapFacturas.put(fac.getNivel().getUsuarioId(), listaFacturas);
		}
		FacturaView fv = new FacturaView();
		fv.setIdFacturaAdm(fac.getFacturaAdm());
		fv.setFechaVencimiento(fac.getVencimiento() != null ? ParseoService
				.getFormattedDate(fac.getVencimiento()) : "");
		listaFacturas.add(fv);
		facturaAdmLongList.add(fac.getFacturaAdm());
	}
	
	private FacturaAdm getFacturaAdm(List<FacturaAdm> facturaAdmList, Long idFacturaAdm){
		for (FacturaAdm facAdm : facturaAdmList) {
			if (facAdm.getId().equals(idFacturaAdm)) {
				return facAdm;
			}
		}
		return null;
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
	
	private void sendPendingMail(String usuarioEmail, String descripcion, String url,
			String asunto, List<FacturaView> facturas, boolean important) {
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
	
	private void sendExpiredMail(String usuarioEmail, String descripcion, String url,
			String asunto, List<FacturaView> facturas, boolean important) {
		mailUtil.javaMailService();
		try {
			Map<String, List<? extends Object>> map = new HashMap<String, List<? extends Object>>();
			map.put("VENCIDAS", facturas);
			Map<String, String> map1 = new HashMap<String, String>();
			map1.put("TITULO", descripcion);
			map1.put("URL", url);
			mailUtil.sendMessage(usuarioEmail, asunto,
					templateLoader.getTemplate("expiredTable", map, map1),
					important);
		} catch (MessagingException e) {
			// Ver que hacer en caso de error
			e.printStackTrace();
		}
	}
	
	private void sendNextExpiredMail(String usuarioEmail, String descripcion, String url,
			String asunto, List<FacturaView> facturas, boolean important) {
		mailUtil.javaMailService();
		try {
			Map<String, List<? extends Object>> map = new HashMap<String, List<? extends Object>>();
			map.put("PROXIMOS", facturas);
			Map<String, String> map1 = new HashMap<String, String>();
			map1.put("TITULO", descripcion);
			map1.put("URL", url);
			mailUtil.sendMessage(usuarioEmail, asunto,
					templateLoader.getTemplate("nextExpiredTable", map, map1),
					important);
		} catch (MessagingException e) {
			// Ver que hacer en caso de error
			e.printStackTrace();
		}
	}
	
	private boolean esVencida(Date fechaVencimiento) {
		Date now = trunkDate(new Date());
		if(now.after(trunkDate(fechaVencimiento))){
			return true;
		}
		return false;
	}

	private boolean venceDiasAntes(Date fechaVencimiento, Long dias) {
		Date now = trunkDate(new Date());
		Date fechaVencimientoTrunk = trunkDate(fechaVencimiento);
		// Calculo la diferencia en días.
		Long diff = (fechaVencimientoTrunk.getTime() -now.getTime())
				/ (MILIS_TO_DAY);
		// Si la fecha actual esta antes que la fecha de vencimiento
		// y la diferencia entre las fechas es igual a la cantidad establecida
		// en dias.
		if ((now.before(fechaVencimientoTrunk) || now
				.equals(fechaVencimientoTrunk)) && diff <= dias) {
			return true;
		}
		return false;
	}
	
	private Date trunkDate(Date date){
		try {
			return sdf.parse(sdf.format(date));
		} catch (ParseException e) {
		}
		return date;
	}

	private Long getNivelesPendientesCount(Factura factura) {
		List<Integer> estados = new ArrayList<Integer>();
		estados.add(EstadosNivel.AUTORIZADO);
		estados.add(EstadosNivel.RECHAZADO);
		estados.add(EstadosNivel.OBSERVADO);
		return nivelPublicadoRepository
				.countNivelesByFacturaIdAndLastAndNivelEstadoNotIn(
						factura.getId(),true,  estados);

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
		//Se deshabilita por ahora....
//		List<Long> estados = new ArrayList<Long>();
//		estados.add(EstadosOC.VIGENTE);
//		estados.add(EstadosOC.EXTENDIDA);
//		for (Orden orden : ordenRepository.findByEstadoInAndSaldoLessThanEqual(
//				estados, Orden.SALDO_LIMITE)) {
//			updateOrdenChangeState(orden, 0L, EstadosOC.CERRADA);
//			sendMail(orden, userRepository.findMailById(orden.getUpdatedBy()),
//					"Orden de Compra Cerrada", true);
//		}
	}

	private void updateOrdenChangeState(Orden orden, Long userId, Long estado) {
		HistorialOC hist = null;
		if (estado == null) {
			hist = AdministradorEstadosOC.cambiarEstado(orden, userId);
		} else {
			hist = AdministradorEstadosOC.cambiarEstado(orden, estado, userId);
		}
		historialRepository.save(hist);
		ordenRepository.save(orden);
	}
	
	private void sendMail(Orden orden, String usuarioEmail, String asunto,
			boolean important) {
		mailUtil.javaMailService();
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("TITULO", "La orden de Compra Nº: " + orden.getNumero()
					+ " se cerro.");
			map.put("TOTALOC",
					"Total orden de compra: $ "
							+ ParseoService.alwaysTwoDecimal(orden.getTotal()));
			map.put("FACTURADO",
					"Total facturado: $ "
							+ ParseoService.alwaysTwoDecimal((orden.getTotal() - orden
									.getSaldo())));
			map.put("SALDO",
					"Saldo actual: $ "
							+ ParseoService.alwaysTwoDecimal(orden.getSaldo()));
			map.put("URL", templateLoader.getUrlBase());
			mailUtil.sendMessage(usuarioEmail, asunto,
					templateLoader.getTemplate("closeOrden", map), important);
		} catch (MessagingException e) {
			// Ver que hacer en caso de error
			e.printStackTrace();
		}
	}
	
}