package com.ostapchuk.tt.hashcat.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.ostapchuk.tt.hashcat.util.Constant.DECRYPTED_QUEUE;
import static com.ostapchuk.tt.hashcat.util.Constant.EMAILS_QUEUE;

@Configuration
public class RabbitMqConfig {

    @Bean(DECRYPTED_QUEUE)
    public Queue decrypted() {
        return new Queue(DECRYPTED_QUEUE, true);
    }

    @Bean(EMAILS_QUEUE)
    public Queue emails() {
        return new Queue(EMAILS_QUEUE, true);
    }
}
