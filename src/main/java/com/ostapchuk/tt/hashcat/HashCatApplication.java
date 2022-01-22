package com.ostapchuk.tt.hashcat;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// TODO: 12/26/2021 add Tests(IT, AT, test containers)

@SpringBootApplication
public class HashCatApplication {

    public static void main(final String[] args) {
        final SpringApplication application = new SpringApplication(HashCatApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }
}
