package com.ostapchuk.tt.hashcat.service;

import com.ostapchuk.tt.hashcat.dto.ApplicationDto;
import com.ostapchuk.tt.hashcat.encryptor.Encryptor;
import com.ostapchuk.tt.hashcat.entity.Application;
import com.ostapchuk.tt.hashcat.entity.Hash;
import com.ostapchuk.tt.hashcat.repository.ApplicationRepository;
import com.ostapchuk.tt.hashcat.repository.HashRepository;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;
import static com.ostapchuk.tt.hashcat.util.Constant.ERROR_CODE_MD5_CLIENT;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Service
@AllArgsConstructor
@ToString
public class HashService {

    private final Encryptor encryptor;
    private final HashRepository hashRepository;
    private final ApplicationRepository applicationRepository;

    @Transactional
    public List<CompletableFuture<Hash>> findOrSaveAll(final ApplicationDto applicationDto) {
        final var decrypted = applicationDto.getHashes();
        final var application = applicationRepository.save(Application.builder()
                                                                   .email(applicationDto.getEmail())
                                                                   .build());
        final var decryptedByExistence = decrypted.stream()
                .collect(Collectors.partitioningBy(hashRepository::existsByDecrypted));
        final var found = decryptedByExistence.get(true)
                .stream().map(d -> hashRepository.save(hashRepository.findByDecrypted(d).addApplication(application)))
                .map(CompletableFuture::completedFuture);
        final var processed = decryptedByExistence.get(false).stream()
                .map(d -> {
                         final var hash = Hash.builder().decrypted(d).build();
                         return hashRepository.save(hash.addApplication(application));
                     }
                ).map(this::process);
        return Stream.concat(processed, found).toList();
    }

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

//    private Hash findOrSave(final String decrypted, final Application application) {
//        return hashRepository.findByDecrypted(decrypted).orElseGet(() -> {
//            final Hash hash = Hash.builder()
//                    .decrypted(decrypted)
//                    .build()
//                    .addApplication(application);
//            return hashRepository.save(hash);
//        });
//    }
}
