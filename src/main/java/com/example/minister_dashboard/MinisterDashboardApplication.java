package com.example.minister_dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MinisterDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinisterDashboardApplication.class, args);
    }

}
