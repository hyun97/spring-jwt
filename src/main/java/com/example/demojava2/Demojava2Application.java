package com.example.demojava2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableEurekaClient
@EnableAspectJAutoProxy
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class Demojava2Application {

    public static void main(String[] args) {
        SpringApplication.run(Demojava2Application.class, args);
    }

}
