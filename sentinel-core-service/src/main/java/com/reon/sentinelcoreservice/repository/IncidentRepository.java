package com.reon.sentinelcoreservice.repository;

import com.reon.sentinelcoreservice.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, String> {
    Optional<Incident> findByTenantIdAndFingerPrint(String tenantId, String fingerPrint);
}
