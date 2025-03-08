package org.Jtech;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HealthCheckApplication {
    private static final Logger logger = LoggerFactory.getLogger(HealthCheckApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(HealthCheckApplication.class, args);
    }


    public void run(String... args) throws Exception {
        logger.info("Application started successfully on port 8080");
    }

}
