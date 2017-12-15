package com.vates.eng.ha.cache;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author Gaston Napoli
 * 
 */
@Configuration
@ImportResource({ "classpath:META-INF/ha/cache-config/ha-cache-context.xml" })
public class HACacheConfig {

}
