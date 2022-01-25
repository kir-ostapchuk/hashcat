package com.ostapchuk.tt.hashcat.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean("hashes")
    public Queue hashes() {
        return new Queue("hashes", true);
    }

    @Bean("emails")
    public Queue emails() {
        return new Queue("emails", true);
    }
}
