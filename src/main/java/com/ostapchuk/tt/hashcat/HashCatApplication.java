package com.ostapchuk.tt.hashcat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class HashCatApplication {
    public static void main(final String[] args) {
        SpringApplication.run(HashCatApplication.class, args);
    }
}
