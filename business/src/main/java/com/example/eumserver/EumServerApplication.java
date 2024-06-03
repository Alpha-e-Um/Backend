package com.example.eumserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EumServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EumServerApplication.class, args);
    }

}
