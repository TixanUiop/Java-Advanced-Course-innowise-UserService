package com.innowise.innowisetasks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaAuditing
@EnableCaching
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.innowise.innowisetasks.repository")
@EntityScan(basePackages = "com.innowise.innowisetasks.entity")
public class InnowiseTasksApplication {

    public static void main(String[] args) {
        SpringApplication.run(InnowiseTasksApplication.class, args);
    }

}
