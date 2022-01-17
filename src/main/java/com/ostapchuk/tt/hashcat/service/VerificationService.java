package com.ostapchuk.tt.hashcat.service;

import com.ostapchuk.tt.hashcat.entity.Client;
import com.ostapchuk.tt.hashcat.repository.ClientRepository;
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

    private final ClientRepository clientRepository;

    private final SenderService senderService;

    public boolean isActive(final String email) {
        return clientRepository.existsByEmailAndActiveIsTrue(email);
    }

    public void sendVerification(final String email) {
        final String uuid = UUID.nameUUIDFromBytes(email.getBytes()).toString();
        clientRepository.save(Client.builder().email(email).linkUuid(uuid).build());
        senderService.send(email, link + uuid, VERIFICATION_MAIL_MESSAGE);
    }

    public boolean verify(final String uuid) {
        AtomicBoolean active = new AtomicBoolean(false);
        final Optional<Client> userOpt = clientRepository.findByLinkUuid(uuid);
        userOpt.ifPresent(u -> {
            u.setActive(true);
            active.set(clientRepository.save(u).isActive());
        });
        return active.get();
    }
}
