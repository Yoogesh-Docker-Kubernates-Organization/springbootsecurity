package com.codetutr.config.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.codetutr.entity.User;

@Configuration
@EnableBatchProcessing
public class AppConfig_Batch {
	 private final String[] FIELD_NAMES = new String[] { "id", "username", "password", "firstName", "lastName" };

	    @Autowired
	    public JobBuilderFactory jobBuilderFactory;

	    @Autowired
	    public StepBuilderFactory stepBuilderFactory;
	    
	    

	    /**
	     * 
	     * Step 1: Define a Reader
	     */
	    @Bean
	    public FlatFileItemReader<UserInput> reader() {
	        return new FlatFileItemReaderBuilder<UserInput>().name("MatchItemReader")
	                .resource(new ClassPathResource("batch/profile.csv")).delimited().names(FIELD_NAMES)
	                .fieldSetMapper(new BeanWrapperFieldSetMapper<UserInput>() {
	                    {
	                        setTargetType(UserInput.class);
	                    }
	                }).build();
	    }

	 
	    /**
	     * 
	     * Step 2: Define a Processor
	     */
	    @Bean
	    public UserDataProcessor processor() {
	        return new UserDataProcessor();
	    }
	    
	    
	    /**
	     * 
	     * Step 3: Writer
	     */
	    @Bean
	    public JdbcBatchItemWriter<User> writer(DataSource dataSource) {
	        return new JdbcBatchItemWriterBuilder<User>()
	                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
	                .sql("INSERT INTO USERS (uid, username, password, firstName, lastName, enabled)"
	                        + " VALUES (:uid, :username, :password, :firstName, :lastName)")
	                .dataSource(dataSource).build();
	    }

	    
	    /**
	     *  Step 4: Define steps: 
	     *  
	     *  :- You can define multiple steps depending upon your requirement
	     *  :- Here we have defined only one step(Just one is sufficient for us for our requirement)
	     *  :- Make sure Every steps should contains three parts i.e. input, process and writer
	     *  
	     */
	    @Bean
	    public Step step1(JdbcBatchItemWriter<User> writer) {
	        return stepBuilderFactory
	            .get("step1")
	            .<UserInput, User>chunk(10) // process 10 items at a time
	            .reader(reader())
	            .processor(processor())
	            .writer(writer)
	            .build();
	    }
	    
	    
	    /**
	     * 
	     * Entry point: Where you combine multiple steps together and send notification once the job finished!!
	     */
	    @Bean
	    public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
	        return jobBuilderFactory
	            .get("importUserJob")
	            .incrementer(new RunIdIncrementer())
	            .listener(listener)
	            .flow(step1)
	            .end()
	            .build();
	    }

}
