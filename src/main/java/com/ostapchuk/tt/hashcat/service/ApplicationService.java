package com.ostapchuk.tt.hashcat.service;

import com.ostapchuk.tt.hashcat.dto.ApplicationDto;
import com.ostapchuk.tt.hashcat.entity.Application;
import com.ostapchuk.tt.hashcat.entity.Hash;
import com.ostapchuk.tt.hashcat.repository.ApplicationRepository;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ApplicationService {

    private final HashService hashService;

    private final RabbitTemplate rabbitTemplate;

    private final ApplicationRepository applicationRepository;

    public void decrypt(final ApplicationDto applicationDto) {
        final var application = applicationRepository.save(Application.builder()
                                                                      .email(applicationDto.getEmail())
                                                                      .build());
        final List<Hash> hashes = hashService.findOrSaveAll(applicationDto.getHashes(), application);
        final long savedAmount = hashes.stream()
                                       .filter(h -> h.getEncrypted() == null)
                                       .count();
        application.setAmount((int) savedAmount);
        applicationRepository.save(application);
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
            rabbitTemplate.convertAndSend("emails", id);
        }
    }
}
