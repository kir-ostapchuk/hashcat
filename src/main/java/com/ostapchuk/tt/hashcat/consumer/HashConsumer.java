package com.ostapchuk.tt.hashcat.consumer;

import com.ostapchuk.tt.hashcat.service.ApplicationService;
import com.ostapchuk.tt.hashcat.service.HashService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.ostapchuk.tt.hashcat.util.Constant.DECRYPTED_QUEUE;

@Component
@RequiredArgsConstructor
@RabbitListener(queues = DECRYPTED_QUEUE)
public class HashConsumer {

    private final HashService hashService;

    private final ApplicationService applicationService;

    @RabbitHandler
    public void listen(final String decrypted) {
        hashService.process(hashService.findByDecrypted(decrypted))
                   .thenAccept(h -> h.getApplications()
                                     .forEach(a -> applicationService.updateAmount(a.getId())));
    }
}
