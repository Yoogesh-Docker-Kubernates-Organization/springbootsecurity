package com.codetutr.config.database;

import java.io.IOException;

import javax.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.hibernate5.HibernateTemplate;

public class HibernateConfig {

	private SessionFactory sessionFactory;;
	
	@Autowired
	public HibernateConfig(EntityManagerFactory entityManagerFactory) {
		this.sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
	}
	
	/**
	 * Hibernate template is deprecated. so it should not be used anymore especially for transaction. (use only for get)
	 */
   	@Bean
    public HibernateTemplate getHibernateTemplate() throws IOException {
    	HibernateTemplate hibernateTemplate = new HibernateTemplate();
    	hibernateTemplate.setSessionFactory(sessionFactory);
    	hibernateTemplate.afterPropertiesSet();
    	return hibernateTemplate;
    }  
}
