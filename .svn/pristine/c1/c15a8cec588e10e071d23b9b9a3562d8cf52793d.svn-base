package com.vates.facpro.persistence.config;

import java.beans.PropertyVetoException;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Persistence configuration.
 * 
 * @author Gaston Napoli
 * 
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.vates.facpro.persistence.repository")
@ComponentScan("com.vates.facpro.persistence")
public class FacProPersistenceConfig {

    private static final String[] PACKAGES_TO_SCAN = new String[] { "com.vates.facpro.persistence.domain" };

    @Value("${init-db:false}")
    private String initDatabase;

    @Value("${jdbc.driverClassName:oracle.jdbc.OracleDriver}")
    private String driverClassName;

    @Value("${jdbc.url:'jdbc:oracle:thin:Vates5:1521:DESA10G'}")
    private String url;

    @Value("${jdbc.username}")
    private String username;

    @Value("${jdbc.password}")
    private String password;

    @Value("${jdbc.maxPoolSize:100}")
    private int maxPoolSize;

    @Value("${jdbc.maxStatements:50}")
    private int maxStatements;

    @Value("${jdbc.minPoolSize:10}")
    private int minPoolSize;

    @Value("${orm.dialect:org.hibernate.dialect.Oracle10gDialect}")
    private String ormDialect;

    @Value("${orm.show_sql:false}")
    private boolean ormShowSql;

    @Value("${orm.format_sql:false}")
    private boolean ormFormatSql;

    @Value("${orm.generate_ddl:false}")
    private boolean ormGenerateDdl;

    @Bean
    public DataSource dataSource() throws PropertyVetoException {
    	  		    	
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName(this.driverClassName);

        dataSource.setUrl(this.url);

        dataSource.setUsername(this.username);

        dataSource.setPassword(this.password);

        dataSource.setMaxActive(this.maxPoolSize);

        dataSource.setMaxIdle(this.minPoolSize);

        return dataSource;

    }

    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {

        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();

        dataSourceInitializer.setDataSource(dataSource);

        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        
//        databasePopulator.addScript(new ClassPathResource("/org/springframework/batch/core/schema-drop-h2.sql"));
//        databasePopulator.addScript(new ClassPathResource("/org/springframework/batch/core/schema-h2.sql"));

        databasePopulator.addScript(new ClassPathResource("db.sql"));

        dataSourceInitializer.setDatabasePopulator(databasePopulator);

        dataSourceInitializer.setEnabled(Boolean.parseBoolean(initDatabase));

        return dataSourceInitializer;

    }

    @Bean
    public EntityManagerFactory entityManagerFactory() throws PropertyVetoException {

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();

        entityManagerFactoryBean.setDataSource(dataSource());

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        vendorAdapter.setGenerateDdl(ormGenerateDdl);

        vendorAdapter.setShowSql(ormShowSql);

        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);

        entityManagerFactoryBean.setPackagesToScan(PACKAGES_TO_SCAN);

        entityManagerFactoryBean.setPersistenceUnitName("facpro");

        entityManagerFactoryBean.setJpaProperties(jpaProperties());

        entityManagerFactoryBean.afterPropertiesSet();

        return entityManagerFactoryBean.getObject();

    }

    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {

        return new HibernateExceptionTranslator();

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

    @Bean
    public PlatformTransactionManager transactionManager() throws PropertyVetoException {

        return new JpaTransactionManager(entityManagerFactory());

    }

}