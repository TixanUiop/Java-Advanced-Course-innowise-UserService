package com.innowise.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;


@EnableJpaAuditing
@EnableCaching
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.innowise.userservice.repository")
@EntityScan(basePackages = "com.innowise.userservice.entity")
@EnableMethodSecurity
public class InnowiseTasksApplication {

    public static void main(String[] args) {
        SpringApplication.run(InnowiseTasksApplication.class, args);
    }

}
