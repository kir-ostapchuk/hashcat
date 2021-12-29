package com.ostapchuk.tt.hashcat.service;

import com.ostapchuk.tt.hashcat.encryptor.Encryptor;
import com.ostapchuk.tt.hashcat.entity.Application;
import com.ostapchuk.tt.hashcat.entity.Hash;
import com.ostapchuk.tt.hashcat.repository.HashRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import static com.ostapchuk.tt.hashcat.util.Constant.ERROR_CODE_MD5_CLIENT;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Service
@AllArgsConstructor
public class HashService {

    private final Encryptor encryptor;
    private final HashRepository hashRepository;

    @Transactional
    public Map<Boolean, List<Hash>> findOrSaveAll(final List<String> decrypted, final Application application) {
        return decrypted.stream()
                .map(d -> findOrSave(d, application))
                .collect(Collectors.partitioningBy(h -> h.getEncrypted() != null));
    }

    @Transactional
    public CompletableFuture<Hash> process(final Hash hash) {
        return encryptor.encrypt(hash)
                .thenApply(r -> {
                    final var body = r.body();
                    if (body.contains(ERROR_CODE_MD5_CLIENT)) {
                        hash.setEncrypted(EMPTY);
                    } else {
                        hash.setEncrypted(body);
                    }
                    return hashRepository.save(hash);
                });
    }

    private Optional<Hash> findByDecrypted(final String decrypted, final Application application) {
        return hashRepository.findByDecrypted(decrypted)
                .map(hash -> hash.addApplication(application));
    }

    private Hash findOrSave(final String decrypted, final Application application) {
        return findByDecrypted(decrypted, application).orElseGet(() -> {
            final Hash hash = Hash.builder()
                    .decrypted(decrypted)
                    .build()
                    .addApplication(application);
            return hashRepository.save(hash);
        });
    }
}
