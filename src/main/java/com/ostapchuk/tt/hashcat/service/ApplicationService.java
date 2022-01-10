package com.ostapchuk.tt.hashcat.service;

import com.ostapchuk.tt.hashcat.dto.ApplicationDto;
import com.ostapchuk.tt.hashcat.entity.Application;
import com.ostapchuk.tt.hashcat.entity.User;
import com.ostapchuk.tt.hashcat.entity.Hash;
import com.ostapchuk.tt.hashcat.repository.ApplicationRepository;
import com.ostapchuk.tt.hashcat.repository.EmailRepository;
import com.ostapchuk.tt.hashcat.service.email.EmailService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import static com.ostapchuk.tt.hashcat.util.Constant.COLON;
import static com.ostapchuk.tt.hashcat.util.Constant.ENCRYPTION_RESULTS;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;

@Service
@AllArgsConstructor
public class ApplicationService {

    private final HashService hashService;

    private final EmailService emailService;

    private final EmailRepository emailRepository;

    private final ApplicationRepository applicationRepository;

    public void decrypt(final ApplicationDto applicationDto, final User user) {
        final Application application = applicationRepository.save(Application.builder()
                                                                           .email(user)
                                                                           .build());
        final var hashes = hashService.findOrSaveAll(applicationDto.getHashes(), application);
        final var saved = hashes.get(false).stream()
                .map(hashService::process);
        final var found = hashes.get(true).stream()
                .map(CompletableFuture::completedFuture);
        final var futures = Stream.concat(saved, found).toList();
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        application.setFinished(true);
        applicationRepository.save(application);
        final AtomicReference<String> message = prepareMessage(futures);
        emailService.send(applicationDto.getEmail(), message.get(), ENCRYPTION_RESULTS);
    }

    public void decrypt(final Long applicationId) {
        final var application = applicationRepository.findById(applicationId).orElseThrow();
        final var hashes = hashService.findOrSaveAll(
                application.getHashes()
                        .stream()
                        .map(Hash::getDecrypted)
                        .collect(Collectors.toList()),
                application);
        final var saved = hashes.get(false).stream()
                .map(hashService::process);
        final var found = hashes.get(true).stream()
                .map(CompletableFuture::completedFuture);
        final var futures = Stream.concat(saved, found).toList();
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        application.setFinished(true);
        applicationRepository.save(application);
        final AtomicReference<String> message = prepareMessage(futures);
        emailService.send(application.getEmail().getMail(), message.get(), ENCRYPTION_RESULTS);
    }

    private AtomicReference<String> prepareMessage(final List<CompletableFuture<Hash>> futures) {
        final AtomicReference<String> message = new AtomicReference<>(EMPTY);
        futures.forEach(cf -> cf.thenAccept(
                hash -> {
                    final var newMsg = hash.getDecrypted() + COLON + SPACE + hash.getEncrypted() +
                                       System.lineSeparator();
                    message.set(message.get() + newMsg);
                }
        ));
        return message;
    }
}
