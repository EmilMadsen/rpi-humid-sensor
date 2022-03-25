package dk.emad.humidsensor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableScheduling
public class Config {

    @Bean
	public TaskExecutor taskExecutor() {
		return new SimpleAsyncTaskExecutor();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
