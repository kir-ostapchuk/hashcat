package com.ostapchuk.tt.hashcat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// TODO: 12/26/2021 add Tests(IT, AT, test containers)
// TODO: 12/26/2021 add Swagger
// TODO: 12/29/2021 check handling of Exception
// TODO: 12/29/2021 check transactional is it really needed
// TODO: 1/3/2022 rework services

@SpringBootApplication
public class HashCatApplication {
    public static void main(final String[] args) {
        SpringApplication.run(HashCatApplication.class, args);
    }
}
