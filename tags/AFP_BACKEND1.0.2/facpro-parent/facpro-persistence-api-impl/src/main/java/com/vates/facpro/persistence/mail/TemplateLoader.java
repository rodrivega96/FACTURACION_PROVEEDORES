package com.vates.facpro.persistence.mail;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring3.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
@ComponentScan("com.vates.facpro.persistence") 
public class TemplateLoader {
	
	@Value("${notifications.server.front.port}")
	private String frontPort;
	
	@Value("${notifications.before.expire.days}")
	private String diasAntesVencimiento;
	
	public String getFrontPortNumber(){
		return frontPort;
	}
	
	public String getDiasAntesVencimiento(){
		return diasAntesVencimiento;
	}
	
	
	public String getTemplate(String nameTemplate, Map<String, String> vars){
	
		ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
		resolver.setPrefix("templates/");
		resolver.setTemplateMode("HTML5");
		resolver.setSuffix(".html");
		TemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(resolver);
		Context context = new Context();
		for(String key:vars.keySet()){
			context.setVariable(key, vars.get(key)); 
			
		}
		return templateEngine.process(nameTemplate, context);
	}
	
	public String getTemplate(String nameTemplate, Map<String, List<? extends Object>> vars, Map<String, String> vars1){
		
		ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
		resolver.setPrefix("templates/");
		resolver.setTemplateMode("HTML5");
		resolver.setSuffix(".html");
		TemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(resolver);
		Context context = new Context();
		for(String key:vars.keySet()){
			context.setVariable(key, vars.get(key)); 
			
		}
		for(String key:vars1.keySet()){
			context.setVariable(key, vars1.get(key)); 
		}
		return templateEngine.process(nameTemplate, context);
	}
	
	public String getTemplateByObject(String nameTemplate, Map<String, ? extends Object> vars, Map<String, String> vars1){
		
		ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
		resolver.setPrefix("templates/");
		resolver.setTemplateMode("HTML5");
		resolver.setSuffix(".html");
		TemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(resolver);
		Context context = new Context();
		for(String key:vars.keySet()){
			context.setVariable(key, vars.get(key)); 
			
		}
		for(String key:vars1.keySet()){
			context.setVariable(key, vars1.get(key)); 
		}
		return templateEngine.process(nameTemplate, context);
	}
	
	public String getUrlBase() {
		InetAddress ip= getCurrentIp();
		String url = ip!=null?ip.getHostAddress():"";
		return "http://" + url + ":" + this.getFrontPortNumber();
	}
	
	private InetAddress getCurrentIp() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) networkInterfaces
                        .nextElement();
                Enumeration<InetAddress> nias = ni.getInetAddresses();
                while(nias.hasMoreElements()) {
                    InetAddress ia= (InetAddress) nias.nextElement();
                    if (!ia.isLinkLocalAddress() 
                     && !ia.isLoopbackAddress()
                     && ia instanceof Inet4Address) {
                        return ia;
                    }
                }
            }
        } catch (SocketException e) {
            return null;
        }
        return null;
    }

}
