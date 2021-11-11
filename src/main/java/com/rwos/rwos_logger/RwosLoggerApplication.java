package com.rwos.rwos_logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class RwosLoggerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RwosLoggerApplication.class, args);
    }

}
