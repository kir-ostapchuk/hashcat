package com.ostapchuk.tt.hashcat.service;

import com.ostapchuk.tt.hashcat.dto.ApplicationDto;
import com.ostapchuk.tt.hashcat.entity.Application;
import com.ostapchuk.tt.hashcat.entity.User;
import com.ostapchuk.tt.hashcat.repository.ApplicationRepository;
import com.ostapchuk.tt.hashcat.repository.EmailRepository;
import com.ostapchuk.tt.hashcat.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationService {

    @Value("${application.link}")
    private String link;

    private final ApplicationService applicationService;
    private final ApplicationRepository applicationRepository;
    private final HashService hashService;

    private final EmailRepository emailRepository;

    private final EmailService emailService;

    public void sendVerificationMail(final ApplicationDto applicationDto) {
        final var optionalEmail = emailRepository.findByMailAndActiveIsTrue(applicationDto.getEmail());
        if (optionalEmail.isEmpty()) {
            final var email = emailRepository.save(User.builder().mail(applicationDto.getEmail()).build());
            final Application application = applicationRepository.save(Application.builder()
                                                                               .email(email)
                                                                               .build());
            hashService.findOrSaveAll(applicationDto.getHashes(), application);
            emailService.send(applicationDto.getEmail(),
                              "Click this link to verify email: " + generateLink(email.getId()),
                              "Email verification");
        } else {
            applicationService.decrypt(applicationDto, optionalEmail.get());
        }
    }

    public boolean verify(final Long id) {
        final var optionalEmail = emailRepository.findById(id);
        if (optionalEmail.isPresent()) {
            final var applications = optionalEmail.get().getApplications();
            applications.stream()
                    .filter(a -> !a.isFinished())
                    .forEach(a -> applicationService.decrypt(a.getId()));
        }
        return optionalEmail.isPresent();
    }

    private String generateLink(final Long id) {
        return link + id;
    }
}
