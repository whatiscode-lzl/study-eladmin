package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author liaozhenglong
 * time 2020/01/25*/

@EnableAsync
@RestController
@SpringBootApplication
@EnableTransactionManagement
public class AppRun {
    public static void main(String[] args) {
        SpringApplication.run(AppRun.class, args);
    }
}
