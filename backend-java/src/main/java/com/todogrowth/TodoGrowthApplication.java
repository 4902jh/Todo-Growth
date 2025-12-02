package com.todogrowth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TodoGrowthApplication {
    public static void main(String[] args) {
        SpringApplication.run(TodoGrowthApplication.class, args);
    }
}

