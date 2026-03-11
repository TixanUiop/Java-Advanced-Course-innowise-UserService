package com.evgeny.innowisetasks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
public class InnowiseTasksApplication {

    public static void main(String[] args) {
        SpringApplication.run(InnowiseTasksApplication.class, args);
    }

}
