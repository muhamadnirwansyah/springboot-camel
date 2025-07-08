package com.app.service_backend;

import com.app.service_backend.config.SftpProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties(SftpProperties.class)
public class ServiceBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceBackendApplication.class, args);
	}

}
