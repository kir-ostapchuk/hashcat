package com.ostapchuk.tt.hashcat.repository;

import com.ostapchuk.tt.hashcat.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByEmailAndActiveIsTrue(String email);

    boolean existsByEmailAndActiveIsTrue(String email);

    Optional<Client> findByLinkUuid(String uuid);

    Client findByEmail(String email);
}
