package com.ostapchuk.tt.hashcat.consumer;

import com.ostapchuk.tt.hashcat.service.HashService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@RabbitListener(queues = "hashes")
public class HashConsumer {

    private final HashService hashService;

    @RabbitHandler
    public void listen(final String decrypted) {
        hashService.process(hashService.findByDecrypted(decrypted));
    }
}
