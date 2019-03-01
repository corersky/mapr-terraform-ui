package com.mapr.ps.cloud.terraform.maprdeployui;

import com.giffing.wicket.spring.boot.starter.app.WicketBootStandardWebApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.inject.Inject;
import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAsync
public class TerraformUiApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder()
				.sources(TerraformUiApplication.class)
				.run(args);
	}

	@Bean
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(1);
		executor.setMaxPoolSize(1);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("Terraform-");
		executor.initialize();
		return executor;
	}

}
