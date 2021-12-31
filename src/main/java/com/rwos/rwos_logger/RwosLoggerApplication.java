package com.rwos.rwos_logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
/*
* authors: Joynath, Ashutosh
*/
@SpringBootApplication
public class RwosLoggerApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(RwosLoggerApplication.class, args);
    }
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(RwosLoggerApplication.class);
    }
}
