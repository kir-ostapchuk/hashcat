package com.ostapchuk.tt.hashcat.repository;

import com.ostapchuk.tt.hashcat.entity.Hash;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashRepository extends CrudRepository<Hash, Long> {

    boolean existsByDecrypted(String decrypted);

    Hash findByDecrypted(String decrypted);
}
