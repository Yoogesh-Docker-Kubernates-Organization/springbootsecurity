package com.codetutr.config.database;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.transaction.TransactionManager;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.codetutr.validationHelper.LemonConstant;

@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Import(value={DatasourceConfig.class, SpringJDBCConfig.class, SpringJPAConfig.class, HibernateConfig.class})
public class AppConfig_Persistance {

	

	@Autowired
	private DataSource datasource; 
	
	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	/**
	 * This {@link TransactionManager} is for {@link SpringJdbc}
	 */
	@Bean(name="springJdbcTransactionManager")
	@ConditionalOnProperty(value=LemonConstant.SPRING_PROFILES_ACTIVE, havingValue = "SpringJdbc", matchIfMissing = false)
	public PlatformTransactionManager getSpringJdbcTransactionManager() {
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
		transactionManager.setDataSource(datasource);
		return transactionManager;
	}
	
	
	/**
	 * This {@link TransactionManager} is for {@link Traditional Hibernate}
	 */
	@Bean(name="hibernateTransactionManager")
	@ConditionalOnProperty(value=LemonConstant.SPRING_PROFILES_ACTIVE, havingValue = "Hibernate", matchIfMissing = false)
    public PlatformTransactionManager getSpringHibernateTransactionManager() {
    	HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(entityManagerFactory.unwrap(SessionFactory.class));
        return transactionManager;
    }
	
	
	/**
	 * This {@link TransactionManager} is for {@link SpringJPA} and {@link SpringData}. Since the {@link SpringData} has internally used a bean name as {@link transactionManager}, 
	 * we cannot rename it.Otherwise it will give an exception.
	 */
	@Bean(name="transactionManager")
	@ConditionalOnProperty(value=LemonConstant.ENABLE_DEFAULT_TRANSACTION_MANAGER, havingValue = "true", matchIfMissing = false)
	public PlatformTransactionManager getSpringJpaTransactionManager() {
		final JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory);
		return transactionManager; 
	}
}
