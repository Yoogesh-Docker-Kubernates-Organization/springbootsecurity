package com.codetutr.config.database;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import com.codetutr.model.DbSetting;

@PropertySource({"classpath:properties/persistence/persistence-${envTarget:local}.properties"})
public class DatasourceConfig {

	private static final String SQL_SCRIPT_PATH = "custom.database.script.path";
	
	@Autowired
	private Environment env;
	
    @Autowired
    private DbSetting dbSetting;

	@Bean
	public DataSource getdataSource() {
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(dbSetting.getDriverClassName());
		dataSource.setUrl(dbSetting.getUrl());
		dataSource.setUsername(dbSetting.getUsername());
		dataSource.setPassword(dbSetting.getPassword());
		return dataSource;
	}
	
	@Bean
	public DataSourceInitializer dataSourceInitializer() {
	    ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
	    /**
	     * This Script will drop a previous database tables and create a fresh tables
	     */
	    resourceDatabasePopulator.addScript(new ClassPathResource(env.getProperty(SQL_SCRIPT_PATH)));
	    DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
	    dataSourceInitializer.setDataSource(getdataSource());
	    dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
	    return dataSourceInitializer;
	}
	
	
	
	/**
	 *  Starting embedded database with following values: </br> 
	 *  {@link url=jdbc:h2:mem:dataSource}</br>
	 *  {@link DB_CLOSE_DELAY=-1}</br>
	 *  {@link DB_CLOSE_ON_EXIT=false}</br>
	 *  {@link username='sa'}</br>
	 *  
	 *   @return DataSource
	 */
	//@Bean
	public DataSource getdataSourceUsingSpringBootDefaultProvidedDatasource_comment_out_above_two_methods_to_use_this() {
		
		/**
		 * No need to shutdown since EmbeddedDatabaseFactoryBean will take care of this
		 */
		return new EmbeddedDatabaseBuilder()
				
				/**
				 * Set DataSource
				 */
				.setName("dataSource")
				
				/**
				 * Lets not get upset as we are only debugging ;-)
				 */
				.ignoreFailedDrops(true).continueOnError(true)
				
				/**
				 * Set the Database
				 */
				.setType(EmbeddedDatabaseType.H2)
				
				/**
				 * Add the SQL script
				 */
				.addScript(env.getProperty(SQL_SCRIPT_PATH)).build();
	}
}
