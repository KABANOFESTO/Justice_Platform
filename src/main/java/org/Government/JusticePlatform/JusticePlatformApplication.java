package org.Government.JusticePlatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = { "org.government.justiceplatform.model" })
@EnableJpaRepositories(basePackages = { "org.government.justiceplatform.repository" })
public class JusticePlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(JusticePlatformApplication.class, args);
	}
}