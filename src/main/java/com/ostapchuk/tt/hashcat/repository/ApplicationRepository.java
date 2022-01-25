package com.ostapchuk.tt.hashcat.repository;

import com.ostapchuk.tt.hashcat.entity.Application;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ApplicationRepository extends CrudRepository<Application, Long> {

    @Transactional
    @Modifying
    @Query("update Application a set a.amount = a.amount - 1 where a.id = :id")
    void decreaseAmount(@Param("id") Long id);
}
