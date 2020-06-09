package com.codetutr.config.database;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@EnableSpringDataWebSupport
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.codetutr.dao")
@PropertySource({"classpath:properties/persistence/persistence-${envTarget:local}.properties"})
public class SpringJPAConfig {
	
	@Autowired
	private DataSource dataSource;
	
    @Autowired
    private Environment env;
	
    @Bean(name="entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setPackagesToScan(new String[] { "com.codetutr.entity" });
        entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactory.setJpaProperties(getAdditionalProperties());
        return entityManagerFactory;
    }
    
    
	@Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
	
	
    public Properties getAdditionalProperties() {
        final Properties hibernateProperties = new Properties();
        /**
         * It will not create a table (thanks to the "update") here if the security-schema.sql script is already run during a dataSource creation
         * If not, then it will create a table
         */
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        hibernateProperties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
        hibernateProperties.setProperty("hibernate.allow_update_outside_transaction", env.getProperty("hibernate.allow_update_outside_transaction"));
        return hibernateProperties;
    }
    
}
