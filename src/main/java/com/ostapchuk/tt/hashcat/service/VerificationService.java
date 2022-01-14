package com.ostapchuk.tt.hashcat.service;

import com.ostapchuk.tt.hashcat.entity.Client;
import com.ostapchuk.tt.hashcat.repository.UserRepository;
import com.ostapchuk.tt.hashcat.service.sender.SenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.ostapchuk.tt.hashcat.util.Constant.VERIFICATION_MAIL_MESSAGE;

@Service
@RequiredArgsConstructor
public class VerificationService {

    @Value("${application.link}")
    private String link;

    private final UserRepository userRepository;

    private final SenderService senderService;

    public boolean isActive(final String email) {
        return userRepository.existsByEmailAndActiveIsTrue(email);
    }

    public void sendVerification(final String email) {
        final String uuid = UUID.nameUUIDFromBytes(email.getBytes()).toString();
        userRepository.save(Client.builder().email(email).linkUuid(uuid).build());
        senderService.send(email, link + uuid, VERIFICATION_MAIL_MESSAGE);
    }

    public boolean verify(final String uuid) {
        AtomicBoolean active = new AtomicBoolean(false);
        final Optional<Client> userOpt = userRepository.findByLinkUuid(uuid);
        userOpt.ifPresent(u -> {
            u.setActive(true);
            active.set(userRepository.save(u).isActive());
        });
        return active.get();
    }

//    private String generateLink(final String email) {
//        return link + UUID.fromString(email);
//    }

//    public void sendVerificationMail(final ApplicationDto applicationDto) {
//        final var optionalEmail = emailRepository.findByMailAndActiveIsTrue(applicationDto.getEmail());
//        if (optionalEmail.isEmpty()) {
//            final var email = emailRepository.save(User.builder().mail(applicationDto.getEmail()).build());
//            final Application application = applicationRepository.save(Application.builder()
//                                                                               .email(email)
//                                                                               .build());
//            hashService.findOrSaveAll(applicationDto.getHashes(), application);
//            emailService.send(applicationDto.getEmail(),
//                              "Click this link to verify email: " + generateLink(email.getId()),
//                              "Email verification");
//        } else {
//            applicationService.decrypt(applicationDto, optionalEmail.get());
//        }
//    }
//
//    public boolean verify(final Long id) {
//        final var optionalEmail = emailRepository.findById(id);
//        if (optionalEmail.isPresent()) {
//            final var applications = optionalEmail.get().getApplications();
//            applications.stream()
//                    .filter(a -> !a.isFinished())
//                    .forEach(a -> applicationService.decrypt(a.getId()));
//        }
//        return optionalEmail.isPresent();
//    }
}
