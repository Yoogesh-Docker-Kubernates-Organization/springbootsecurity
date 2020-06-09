package com.codetutr.config.cronJob;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * This combination of annotation sets up spring to go ahead and perform task out of the task pool.
 */
@EnableScheduling
@EnableAsync
@Import(value={MyJob.class})
public class AppConfig_CronJob {
	
	@Bean
	TaskExecutor getTaskExecutor() {
		return new SimpleAsyncTaskExecutor();
	}
}
 