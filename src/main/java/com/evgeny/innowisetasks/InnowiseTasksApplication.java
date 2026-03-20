package com.evgeny.innowisetasks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;


@EnableJpaAuditing
@EnableCaching
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.evgeny.innowisetasks.Repository")
@EntityScan(basePackages = "com.evgeny.innowisetasks.Entity")
public class InnowiseTasksApplication {

    public static void main(String[] args) {
        SpringApplication.run(InnowiseTasksApplication.class, args);
    }

}
