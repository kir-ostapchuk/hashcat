package com.ostapchuk.tt.hashcat.repository;

import com.ostapchuk.tt.hashcat.entity.Hash;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashRepository extends CrudRepository<Hash, Long> {

    boolean existsByDecrypted(String decrypted);

    Optional<Hash> findByDecrypted(String decrypted);

    Optional<Hash> findByDecryptedAndEncryptedNull(String decrypted);
}
