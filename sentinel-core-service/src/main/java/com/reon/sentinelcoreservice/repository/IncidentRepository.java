package com.reon.sentinelcoreservice.repository;

import com.reon.sentinelcoreservice.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, String> {
    Optional<Incident> findByTenantIdAndFingerprint(String tenantId, String fingerprint);

    @Modifying
    @Query(
            "UPDATE Incident i " +
            "SET i.occurrenceCount = i.occurrenceCount + 1, " +
                    "i.lastSeen = :now " +
            "WHERE i.tenantId = :tenantId AND i.fingerprint = :fingerprint"
    )
    int incrementOccurrence(@Param("tenantId") String tenantId,
                            @Param("fingerprint") String fingerprint,
                            @Param("now") Instant now);
}
