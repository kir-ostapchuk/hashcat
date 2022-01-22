package com.ostapchuk.tt.hashcat.service;

import com.ostapchuk.tt.hashcat.dto.ApplicationDto;
import com.ostapchuk.tt.hashcat.entity.Application;
import com.ostapchuk.tt.hashcat.entity.Hash;
import com.ostapchuk.tt.hashcat.repository.ApplicationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
        // TODO: 1/22/22 do not process after saving. create service that will process hashes only from the queue 
//        final var saved = hashes.get(false).stream()
//                                .map(hashService::process);
//        final var found = hashes.get(true).stream()
//                                .map(CompletableFuture::completedFuture);
//        final var futures = Stream.concat(saved, found).toList();
//        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        return new ArrayList<>();
//        return futures;
    }
}
