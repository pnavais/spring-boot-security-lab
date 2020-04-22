package com.github.pnavais.lab;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.github.pnavais.lab.student.repository")
public class StudentApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(StudentApplication.class)
				.properties("spring.config.name:application,db")
				.build()
				.run(args);
	}

}