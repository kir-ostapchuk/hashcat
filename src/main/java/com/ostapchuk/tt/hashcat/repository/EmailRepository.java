package com.ostapchuk.tt.hashcat.repository;

import com.ostapchuk.tt.hashcat.entity.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends CrudRepository<User, Long> {

    Optional<User> findByMailAndActiveIsTrue(String email);
    Optional<User> findByMail(String email);
}
