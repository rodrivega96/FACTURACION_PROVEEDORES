package com.vates.facpro.persistence.mail;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

@Configuration
@ComponentScan("com.vates.facpro.persistence")
public class MailUtil {

	@Value("${mail.host}")
	private String host;

	@Value("${mail.port}")
	private Integer port;

	@Value("${mail.pass}")
	private String password;

	@Value("${mail.user}")
	private String username;

	@Value("${mail.address}")
	private String address;

	@Value("${mail.starttls.enable}")
	private String starttls;

	@Value("${mail.auth}")
	private String auth;

	@Value("${mail.transport.protocol}")
	private String protocol;

	private MimeMessage message;
	private MimeMessageHelper messageHelper;
	private Transport transport;

	private static String SINCRONO = "SINCRONO";

	@Bean
	public JavaMailSender javaMailService() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

		javaMailSender.setHost(host);
		javaMailSender.setPort(port);
		javaMailSender.setPassword(password);
		javaMailSender.setUsername(username);

		javaMailSender.setJavaMailProperties(getMailProperties());

		Session session = Session.getDefaultInstance(this.getMailProperties(),
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});
		session.setDebug(true);

		javaMailSender.setSession(session);

		message = javaMailSender.createMimeMessage();

		messageHelper = new MimeMessageHelper(message);

		try {
			InternetAddress addressFrom;
			addressFrom = new InternetAddress(address, username);
			message.setSender(addressFrom);
			message.setFrom(addressFrom);
			transport = session.getTransport();

		} catch (AddressException e1) {
			e1.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return javaMailSender;
	}

	@SuppressWarnings("static-access")
	public void sendMessage(String[] tos, String subject, String text) {
		try {
			synchronized (SINCRONO) {
				for (String to : tos) {
					message.setSubject(subject);
					message.setContent(subject, "text/html; charset=utf-8");
					message.setRecipients(Message.RecipientType.TO, to);

					if (transport.isConnected()) {
						transport.close();
					}

					transport.connect();
					transport.send(message);
				}
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		} finally {
			try {
				transport.close();
			} catch (MessagingException e) {
				transport = null;
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("static-access")
	public void sendMessage(String to, List<String> ccs, String subject,
			String text, boolean important) throws MessagingException {
		try {
			synchronized (SINCRONO) {
				if (important) {
					messageHelper.setPriority(1);
				}
				
				message.setSubject(subject);
				message.setContent(text, "text/html; charset=utf-8");
				message.setRecipients(Message.RecipientType.TO, to);
				message.setRecipients(Message.RecipientType.CC, "");
				for (String cc : ccs) {
					message.addRecipients(Message.RecipientType.CC, cc);
				}

				if (transport.isConnected()) {
					transport.close();
				}
				transport.connect();
				transport.send(message);
			}

		} catch (Exception e) {
			throw new MessagingException();
		} finally {
			try {
				transport.close();
			} catch (MessagingException e) {
				transport = null;
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("static-access")
	public void sendMessage(String to, String subject, String text,
			boolean important) throws MessagingException {
		try {

			if (important) {
				messageHelper.setPriority(1);
			}

			message.setSubject(subject);
			message.setContent(text, "text/html; charset=utf-8");
			message.setRecipients(Message.RecipientType.TO, to);
			if (Boolean.getBoolean(auth)) {
				transport.connect();
			}
			transport.send(message);

		} catch (Exception e) {
			throw new MessagingException();
		} finally {
			try {
				transport.close();
			} catch (MessagingException e) {
				transport = null;
				e.printStackTrace();
			}
		}
	}

	private Properties getMailProperties() {
		Properties properties = new Properties();
		properties.setProperty("mail.transport.protocol", protocol);
		properties.setProperty("mail.smtp.auth", auth);
		properties.setProperty("mail.smtp.starttls.enable", starttls);
		properties.setProperty("mail.debug", "true");
		properties.setProperty("mail.host", host);

		return properties;
	}

}