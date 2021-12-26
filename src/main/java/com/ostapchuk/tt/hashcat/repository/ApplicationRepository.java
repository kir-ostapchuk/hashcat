package com.ostapchuk.tt.hashcat.repository;

import com.ostapchuk.tt.hashcat.entity.Application;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends CrudRepository<Application, Long> {
}
