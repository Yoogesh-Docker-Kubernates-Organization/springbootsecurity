package com.codetutr.config.batch;

import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;

import com.codetutr.entity.User;
import com.codetutr.populatorHelper.Converter;
import com.codetutr.services.UserService;

@Profile({"Mock", "SpringDataJPA", "SpringEmJPA"})
@EnableBatchProcessing  // This is by default providing its own transactionManager which we are overriding by our. So that "SpringJdbc" and "Hibernate" profile only works.otherwise its transactionManager comes.
public class AppConfig_Batch {
	
	private final String[] FIELD_NAMES = new String[] { "id", "username", "password", "firstName", "lastName" };

	@Autowired
	private DataSource datasource; 
	
	@Autowired
	@Qualifier(value="transactionManager")
	private PlatformTransactionManager transactionManager;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	UserService service;
	
	@Autowired
	Converter<UserInput, User> userConverter;
	
    
    private JobRepository getJobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(datasource);
        factory.setTransactionManager(transactionManager);
        factory.afterPropertiesSet();
        return (JobRepository) factory.getObject();
    }
	
    @Bean
	public JobLauncher getJobLauncher() throws Exception {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(getJobRepository());
		jobLauncher.afterPropertiesSet();
		return jobLauncher;
	}
	
	@Bean
	public JobBuilderFactory jobBuilderFactory() throws Exception {
	    JobBuilderFactory jobBuilderFactory = new JobBuilderFactory(getJobRepository());
	    return jobBuilderFactory;
	}
	
	@Bean
	public StepBuilderFactory stepBuilderFactory() throws Exception {
		StepBuilderFactory setBuilderFactory = new StepBuilderFactory(getJobRepository(), transactionManager);
		return setBuilderFactory;
	}
    
	
	/**
	 * It is defined at BatchAutoConfiguration.class and i am reusing here just for the informational purpose
	 * This is the main luncher which lunches the job. By default it runs at the startup. i have disalbed it via application.properties file
	 * By providing "spring.batch.job.enabled=false" so that the bean never created and job is lunched
	 */
	@Bean
	@ConditionalOnProperty(prefix = "spring.batch.job", name = "enabled", havingValue = "true", matchIfMissing = true)
	public JobLauncherApplicationRunner jobLauncherApplicationRunner(JobLauncher jobLauncher, JobExplorer jobExplorer,
			JobRepository jobRepository, BatchProperties properties) {
		JobLauncherApplicationRunner runner = new JobLauncherApplicationRunner(jobLauncher, jobExplorer, jobRepository);
		String jobNames = properties.getJob().getNames();
		if (StringUtils.hasText(jobNames)) {
			runner.setJobNames(jobNames);
		}
		return runner;
	}
    

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
        return new UserDataProcessor(userConverter);
    }
    
    
    /**
     * 
     * Step 3: Define a Writer
     */
    private class DatabaseWriter implements ItemWriter<User> {
		@Override
		public void write(List<? extends User> items) throws Exception {
			for (User user : items) {
				service.createUser(user);
			}
			
		}
    } 
    
    
    /**
     *  Step 4: Define steps: 
     *  
     *  :- You can define multiple steps depending upon your requirement
     *  :- Here we have defined only one step(Just one is sufficient for us for our requirement)
     *  :- Make sure Every steps should contains three parts i.e. input, process and writer
     * @throws Exception 
     *  
     */
    @Bean
    public Step step1() throws Exception {
        return stepBuilderFactory()
            .get("step1")
            .<UserInput, User>chunk(10) // process 10 items at a time
            .reader(reader())
            .processor(processor())
            .writer(new DatabaseWriter())
            .build();
    }
    
    
    /**
     * 
     * This is a Main Entry point: Where you combine multiple steps together and send notification once the job finished!!
     */
    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step1) throws Exception {
        return jobBuilderFactory()
            .get("importUserJob")
            .incrementer(new RunIdIncrementer())
            .listener(listener)
            .flow(step1)
            .end()
            .build();
    }    
}
