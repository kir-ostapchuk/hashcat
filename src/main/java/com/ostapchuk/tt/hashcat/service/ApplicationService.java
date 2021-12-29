package com.ostapchuk.tt.hashcat.service;

import com.ostapchuk.tt.hashcat.dto.ApplicationDto;
import com.ostapchuk.tt.hashcat.entity.Hash;
import com.ostapchuk.tt.hashcat.service.email.EmailService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
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

    public void decrypt(final ApplicationDto applicationDto) {
        CompletableFuture.runAsync(() -> {
            final var futures = hashService.findOrSaveAll(applicationDto);
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            final AtomicReference<String> message = prepareMessage(futures);
            emailService.send(applicationDto.getEmail(), message.get(), ENCRYPTION_RESULTS);
        });
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
