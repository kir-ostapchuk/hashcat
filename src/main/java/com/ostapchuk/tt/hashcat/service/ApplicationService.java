package com.ostapchuk.tt.hashcat.service;

import com.ostapchuk.tt.hashcat.dto.ApplicationDto;
import com.ostapchuk.tt.hashcat.entity.Application;
import com.ostapchuk.tt.hashcat.entity.Hash;
import com.ostapchuk.tt.hashcat.repository.ApplicationRepository;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ApplicationService {

    private final HashService hashService;

    private final ApplicationRepository applicationRepository;

    public List<CompletableFuture<Hash>> decrypt(final ApplicationDto applicationDto) {
        final var application = applicationRepository.save(Application.builder()
                                                                   .email(applicationDto.getEmail())
                                                                   .build());
        final var hashes = hashService.findOrSaveAll(applicationDto.getHashes(), application);
        final var saved = hashes.get(false).stream()
                .map(hashService::process);
        final var found = hashes.get(true).stream()
                .map(CompletableFuture::completedFuture);
        final var futures = Stream.concat(saved, found).toList();
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        return futures;
    }
}
