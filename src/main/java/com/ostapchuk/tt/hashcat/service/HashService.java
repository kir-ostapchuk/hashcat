package com.ostapchuk.tt.hashcat.service;

import com.ostapchuk.tt.hashcat.encryptor.Encryptor;
import com.ostapchuk.tt.hashcat.entity.Application;
import com.ostapchuk.tt.hashcat.entity.Hash;
import com.ostapchuk.tt.hashcat.repository.ApplicationRepository;
import com.ostapchuk.tt.hashcat.repository.HashRepository;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.ostapchuk.tt.hashcat.util.Constant.DECRYPTED_QUEUE;
import static com.ostapchuk.tt.hashcat.util.Constant.ERROR_CODE_MD5_CLIENT;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Service
@Slf4j
@AllArgsConstructor
@ToString
public class HashService {

    private final RabbitTemplate rabbitTemplate;

    private final Encryptor encryptor;

    private final HashRepository hashRepository;

    private final ApplicationRepository applicationRepository;

    @Transactional
    public CompletableFuture<Hash> process(final Hash hash) {
        return encryptor.encrypt(hash)
                        .thenApply(r -> {
                            final var body = r.body();
                            if (body.contains(ERROR_CODE_MD5_CLIENT)) {
                                log.info("Can not get hash from the external service. Error: " + body);
                                hash.setEncrypted(EMPTY);
                            } else {
                                hash.setEncrypted(body);
                            }
//                            hash.getApplications().forEach(a -> applicationRepository.decreaseAmount(a.getId()));
//                            hash.getApplications().forEach(a -> a.setAmount(a.getAmount() - 1));
                            return hashRepository.save(hash);
                        });
    }

    // TODO: 1/22/22 replace boolean with status maybe
    // TODO: 1/22/22 check for spaces in the decrypted
    @Transactional
    public List<Hash> findOrSaveAll(final List<String> decrypted, final Application application) {
        return decrypted.stream()
                        .map(d -> findOrSave(d, application))
                        .toList();
    }

    public Hash findOrSave(final String decrypted, final Application application) {
        // TODO: 1/22/22 or Else send message to queue
        return findByDecrypted(decrypted, application).orElseGet(() -> {
            final Hash hash = Hash.builder()
                                  .decrypted(decrypted)
                                  .build()
                                  .addApplication(application);
            final Hash saved = hashRepository.save(hash);
            rabbitTemplate.convertAndSend(DECRYPTED_QUEUE, decrypted);
            return saved;
        });
    }

    public Hash findByDecrypted(final String decrypted) {
        // TODO: 1/22/22 redo
        return hashRepository.findByDecrypted(decrypted).orElseThrow();
    }

    private Optional<Hash> findByDecrypted(final String decrypted, final Application application) {
        return hashRepository.findByDecrypted(decrypted)
                             .map(hash -> hash.addApplication(application));
    }
}
