package com.ostapchuk.tt.hashcat.service;

import com.ostapchuk.tt.hashcat.encryptor.Encryptor;
import com.ostapchuk.tt.hashcat.entity.Application;
import com.ostapchuk.tt.hashcat.entity.Hash;
import com.ostapchuk.tt.hashcat.repository.HashRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.ostapchuk.tt.hashcat.util.Constant.ERROR_CODE_MD5_CLIENT;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Service
@Slf4j
@AllArgsConstructor
public class HashService {

    private final RabbitTemplate rabbitTemplate;

    private final Encryptor encryptor;

    private final HashRepository hashRepository;

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
                            return hashRepository.save(hash);
                        });
    }

    // TODO: 1/22/22 replace boolean with status maybe
    // TODO: 1/22/22 check for spaces in the decrypted
    @Transactional
    public Map<Boolean, List<Hash>> findOrSaveAll(final List<String> decrypted, final Application application) {
        return decrypted.stream()
                        .map(d -> findOrSave(d.trim(), application))
                        .collect(Collectors.partitioningBy(h -> h.getEncrypted() != null));
    }

    public Hash findOrSave(final String decrypted, final Application application) {
        // TODO: 1/22/22 or Else send message to queue
        return findByDecrypted(decrypted, application).orElseGet(() -> {
            rabbitTemplate.convertAndSend("hashes", decrypted);
            final Hash hash = Hash.builder()
                                  .decrypted(decrypted)
                                  .build()
                                  .addApplication(application);
            return hashRepository.save(hash);
//            return new Hash();
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
