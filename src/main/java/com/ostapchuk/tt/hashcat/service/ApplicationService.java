package com.ostapchuk.tt.hashcat.service;

import com.ostapchuk.tt.hashcat.dto.ApplicationDto;
import com.ostapchuk.tt.hashcat.entity.Application;
import com.ostapchuk.tt.hashcat.entity.Hash;
import com.ostapchuk.tt.hashcat.repository.ApplicationRepository;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ostapchuk.tt.hashcat.util.Constant.EMAILS_QUEUE;

@Service
@AllArgsConstructor
public class ApplicationService {

    private final HashService hashService;

    private final RabbitTemplate rabbitTemplate;

    private final ApplicationRepository applicationRepository;

    // TODO: 1/25/22 simplify the logic
    @Async
    public void decrypt(final ApplicationDto applicationDto) {
        final var application = applicationRepository.save(Application.builder()
                                                                      .email(applicationDto.getEmail())
                                                                      .build());
        final List<Hash> hashes = hashService.findOrSaveAll(applicationDto.getHashes(), application);
        final long savedAmount = hashes.stream()
                                       .filter(h -> h.getEncrypted() == null)
                                       .count();
        if (savedAmount == 0) {
            rabbitTemplate.convertAndSend(EMAILS_QUEUE, application.getId());
        } else {
            application.setAmount((int) savedAmount);
            applicationRepository.save(application);
        }
    }

    public Application findById(final Long id) {
        return applicationRepository.findById(id).orElseThrow();
    }

    public void updateAmount(final Long id) {
        applicationRepository.decreaseAmount(id);
        final Integer amount = applicationRepository.findById(id)
                                                    .map(Application::getAmount)
                                                    .orElse(-1);
        if (amount == 0) {
            rabbitTemplate.convertAndSend(EMAILS_QUEUE, id);
        }
    }
}
