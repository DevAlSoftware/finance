package com.devalFinance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.devalFinance.infrastructure.persistence.repository")
public class DevalFinanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevalFinanceApplication.class, args);
	}

}
