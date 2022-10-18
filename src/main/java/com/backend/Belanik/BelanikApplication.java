package com.backend.Belanik;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.backend")
@EnableJpaRepositories
@EnableTransactionManagement
public class BelanikApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(BelanikApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(BelanikApplication.class);
	}
}
