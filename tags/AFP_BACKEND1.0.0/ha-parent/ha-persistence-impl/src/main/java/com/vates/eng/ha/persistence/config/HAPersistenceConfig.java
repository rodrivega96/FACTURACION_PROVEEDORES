package com.vates.eng.ha.persistence.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
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
@EnableJpaRepositories(basePackages = { "com.vates.eng.ha.persistence.repository" }, entityManagerFactoryRef = "ha.entityManagerFactory", transactionManagerRef = "ha.transactionManager")
@ComponentScan("com.vates.eng.ha.persistence")
@Profile("ha.db.prod")
public class HAPersistenceConfig {

    private static final String PACKAGES_TO_SCAN = "com.vates.eng.ha.persistence.domain";

    @Value("${orm.dialect:org.hibernate.dialect.MySQL5InnoDBDialect}")
    private String ormDialect;

    @Value("${orm.format_sql:false}")
    private boolean ormFormatSql;

    @Value("${orm.show_sql:false}")
    private boolean ormShowSql;

    @Value("${orm.generate_ddl:false}")
    private boolean ormGenerateDdl;

    @Value("${jndi.name:jdbc/ha}")
    private String jndiName;

    @Bean(name = "ha.dataSource")
    public DataSource dataSource() {

        final JndiDataSourceLookup datasourceLookup = new JndiDataSourceLookup();

        datasourceLookup.setResourceRef(true);

        DataSource dataSource = datasourceLookup.getDataSource(this.jndiName);

        return dataSource;

    }

    @Bean(name = "ha.entityManagerFactory")
    public EntityManagerFactory entityManagerFactory() {

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();

        entityManagerFactoryBean.setDataSource(dataSource());

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        vendorAdapter.setGenerateDdl(ormGenerateDdl);

        vendorAdapter.setShowSql(ormShowSql);

        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);

        entityManagerFactoryBean.setPackagesToScan(PACKAGES_TO_SCAN);

        entityManagerFactoryBean.setPersistenceUnitName("ha-prod");

        entityManagerFactoryBean.setJpaProperties(jpaProperties());

        entityManagerFactoryBean.afterPropertiesSet();

        return entityManagerFactoryBean.getObject();

    }

    Properties jpaProperties() {

        Properties jpaProperties = new Properties();

        jpaProperties.put("hibernate.query.substitutions", "true 'Y', false 'N'");

        jpaProperties.put("hibernate.show_sql", ormShowSql);

        jpaProperties.put("hibernate.format_sql", ormFormatSql);

        jpaProperties.put("hibernate.dialect", ormDialect);

        jpaProperties.put("hibernate.cache.use_second_level_cache", true);

        jpaProperties.put("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory");

        jpaProperties.put("hibernate.cache.use_query_cache", true);

        jpaProperties.put("hibernate.generate_statistics", true);

        return jpaProperties;

    }

    @Bean(name = "ha.hibernateExceptionTranslator")
    public HibernateExceptionTranslator hibernateExceptionTranslator() {

        return new HibernateExceptionTranslator();

    }

    @Bean(name = "ha.transactionManager")
    public PlatformTransactionManager transactionManager() {

        return new JpaTransactionManager(entityManagerFactory());

    }

}