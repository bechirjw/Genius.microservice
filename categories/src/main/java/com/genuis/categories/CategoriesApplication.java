package com.genuis.categories;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class CategoriesApplication {

    public static void main(String[] args) {
        SpringApplication.run(CategoriesApplication.class, args);
    }

}
