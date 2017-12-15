package com.vates.eng.ha.persistence;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Gaston Napoli
 * 
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = { "com.vates.eng.ha.persistence.repository" }, entityManagerFactoryRef = "ha.test.entityManagerFactory", transactionManagerRef = "ha.test.transactionManager")
@ComponentScan("com.vates.eng.ha.persistence")
@Profile("ha.db.test")
public class TestPersistenceConfig {

    @Bean(name = "ha.test.dataSource")
    public DataSource dataSource() {

        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();

    }

    @Bean(name = "ha.test.hibernateExceptionTranslator")
    public HibernateExceptionTranslator hibernateExceptionTranslator() {

        return new HibernateExceptionTranslator();

    }

    @Bean(name = "ha.test.transactionManager")
    @DependsOn("ha.test.entityManagerFactory")
    public PlatformTransactionManager transactionManager() {

        return new JpaTransactionManager(entityManagerFactory());

    }

    @Bean(name = "ha.test.entityManager")
    public EntityManager entityManager() {

        return entityManagerFactory().createEntityManager();

    }

    @Bean(name = "ha.test.entityManagerFactory")
    public EntityManagerFactory entityManagerFactory() {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        vendorAdapter.setGenerateDdl(Boolean.TRUE);

        vendorAdapter.setShowSql(Boolean.FALSE);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

        factory.setPersistenceUnitName("ha-test");

        factory.setJpaVendorAdapter(vendorAdapter);

        factory.setPackagesToScan("com.vates.eng.ha.persistence.domain");

        factory.setDataSource(dataSource());

        factory.setJpaProperties(jpaProperties());

        factory.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());

        factory.afterPropertiesSet();

        return factory.getObject();

    }

    Properties jpaProperties() {

        Properties props = new Properties();

        props.put("hibernate.query.substitutions", "true 'Y', false 'N'");

        props.put("hibernate.hbm2ddl.auto", "create-drop");

        props.put("hibernate.show_sql", "true");

        props.put("hibernate.format_sql", "false");

        return props;

    }

    @Bean
    public BeanFactoryPostProcessor beanFactoryPostProcessor() {

        PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();

        configurer.setLocations(new Resource[] { new ClassPathResource("/ha-conf-test.properties") });

        return configurer;

    }

}
