package com.ostapchuk.tt.hashcat.controller;

import com.ostapchuk.tt.hashcat.dto.ApplicationDto;
import com.ostapchuk.tt.hashcat.service.ApplicationService;
import com.ostapchuk.tt.hashcat.service.VerificationService;
import com.ostapchuk.tt.hashcat.service.sender.SenderService;
import com.ostapchuk.tt.hashcat.util.SenderUtil;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.ostapchuk.tt.hashcat.util.Constant.ENCRYPTION_RESULTS;
import static org.springframework.http.HttpStatus.ACCEPTED;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    private final SenderService senderService;

    private final VerificationService verificationService;

    // TODO: 1/14/22 when email is verified calculate hashes from the previous application
    // TODO: 1/14/22 do not send email or regenerate uuid if the user exists but not active
    // TODO: 1/13/22 return not 202

    @Async
    @PostMapping
    @ResponseStatus(ACCEPTED)
    public void decrypt(@Validated @RequestBody final ApplicationDto applicationDto) {
        final String email = applicationDto.getEmail();
        if (verificationService.isActive(email)) {
            final var futures = applicationService.decrypt(applicationDto);
            final var message = SenderUtil.prepareMessage(futures);
            senderService.send(email, message.get(), ENCRYPTION_RESULTS);
        } else {
            verificationService.sendVerification(email);
        }
    }
}

