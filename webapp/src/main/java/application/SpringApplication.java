package application;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "abstraction")
@ComponentScan(basePackages = "main")
@ComponentScan(basePackages = "application.controller")
/**
 * SpringBootApplication - web application
 **/
@SpringBootApplication
public class SpringApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(SpringApplication.class, args);
    }
}
