package org.example.clickservice;

import org.example.clickservice.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class ClickServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClickServiceApplication.class, args);
    }
}