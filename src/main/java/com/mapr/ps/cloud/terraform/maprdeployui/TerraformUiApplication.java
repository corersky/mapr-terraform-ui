package com.mapr.ps.cloud.terraform.maprdeployui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class TerraformUiApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder()
				.sources(TerraformUiApplication.class)
				.run(args);
	}

}
