package com.ostapchuk.tt.hashcat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// TODO: 12/26/2021 add Liquibase
// TODO: 12/26/2021 add Profiles
// TODO: 12/26/2021 add Tests
// TODO: 12/26/2021 add Swagger
// TODO: 12/29/2021 check handling of Exception

@SpringBootApplication
public class HashCatApplication {
    public static void main(final String[] args) {
        SpringApplication.run(HashCatApplication.class, args);
    }
}
