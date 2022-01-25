package com.ostapchuk.tt.hashcat.consumer;

import com.ostapchuk.tt.hashcat.entity.Application;
import com.ostapchuk.tt.hashcat.service.ApplicationService;
import com.ostapchuk.tt.hashcat.service.sender.SenderService;
import com.ostapchuk.tt.hashcat.util.SenderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.ostapchuk.tt.hashcat.util.Constant.EMAILS_QUEUE;
import static com.ostapchuk.tt.hashcat.util.Constant.ENCRYPTION_RESULTS;

@Component
@RequiredArgsConstructor
@RabbitListener(queues = EMAILS_QUEUE)
public class EmailConsumer {

    private final SenderService senderService;

    private final ApplicationService applicationService;

    @RabbitHandler
    public void listen(final Long id) {
        final Application application = applicationService.findById(id);
        final var message = SenderUtil.prepareMessageNew(application.getHashes());
        senderService.send(application.getEmail(), message, ENCRYPTION_RESULTS);
    }
}
