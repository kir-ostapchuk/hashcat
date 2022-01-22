package com.ostapchuk.tt.hashcat.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public Queue myQueue() {
        return new Queue("hashes", false);
    }
}
