package com.ostapchuk.tt.hashcat.controller;

import com.ostapchuk.tt.hashcat.dto.ApplicationDto;
import com.ostapchuk.tt.hashcat.service.ApplicationService;
import com.ostapchuk.tt.hashcat.service.sender.SenderService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

import static org.springframework.http.HttpStatus.ACCEPTED;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    private final SenderService senderService;

    @PostMapping
    @ResponseStatus(ACCEPTED)
    public void decrypt(@Validated @RequestBody final ApplicationDto applicationDto) {
        CompletableFuture.runAsync(() -> {
            final var futures = applicationService.decrypt(applicationDto);
//            final var message = SenderUtil.prepareMessage(futures);
//            senderService.send(applicationDto.getEmail(), message.get(), ENCRYPTION_RESULTS);
        });
    }
}

