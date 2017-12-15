package com.vates.eng.ha.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.vates.eng.ha.util.impl.MessageResourceBundleImpl;

/**
 * @author Gaston Napoli
 * 
 */
@Configuration
@ImportResource({ "classpath:META-INF/cxf/cxf.xml", "classpath:META-INF/ha/core-config/ha-core-context.xml" })
@ComponentScan("com.vates.eng.ha")
public class HACoreConfig {

    @Bean
    ResourceBundleMessageSource messageSource() {

        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

        messageSource.setBasename("locale/messages");

        return messageSource;

    }

    @Bean
    MessageSourceAccessor messageSourceAccessor() {

        return new MessageSourceAccessor(this.messageSource());

    }

    @Bean
    MessageResourceBundleImpl messageResource() {

        return new MessageResourceBundleImpl(this.messageSourceAccessor());

    }

}
