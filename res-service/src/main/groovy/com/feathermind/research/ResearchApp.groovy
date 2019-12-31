package com.feathermind.research

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
@EnableTransactionManagement
class ResearchApp {
	static void main(String[] args) {
		SpringApplication.run(ResearchApp, args)
	}
}
