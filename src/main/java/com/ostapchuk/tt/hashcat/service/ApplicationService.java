package com.ostapchuk.tt.hashcat.service;

import com.ostapchuk.tt.hashcat.dto.ApplicationDto;
import com.ostapchuk.tt.hashcat.entity.Application;
import com.ostapchuk.tt.hashcat.entity.Client;
import com.ostapchuk.tt.hashcat.entity.Hash;
import com.ostapchuk.tt.hashcat.repository.ApplicationRepository;
import com.ostapchuk.tt.hashcat.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class ApplicationService {

    private final HashService hashService;

    private final UserRepository userRepository;

    private final ApplicationRepository applicationRepository;

    public List<CompletableFuture<Hash>> decrypt(final ApplicationDto applicationDto) {
        final Client client = userRepository.findByEmail(applicationDto.getEmail());
        final var application = applicationRepository.save(Application.builder()
                                                                      .client(client)
                                                                      .build());
//        userRepository.save(user.addApplication(application));
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
