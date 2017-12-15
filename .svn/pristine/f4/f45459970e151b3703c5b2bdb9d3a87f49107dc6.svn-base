package com.vates.facpro.persistence.mail;

import java.util.Map;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring3.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
@ComponentScan("com.vates.facpro.persistence") 
public class TemplateLoader {
	
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

}
