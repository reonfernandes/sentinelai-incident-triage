package com.reon.sentinelcoreservice.entity;

import com.reon.dto.Severity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
        name = "incidents",
        uniqueConstraints = @UniqueConstraint(
                name = "unq_tenant_fingerprint",
                columnNames = {"tenant_id", "fingerprint"}
        )
)
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "tenant_id", nullable = false, updatable = false)
    private String tenantId;

    @Column(nullable = false)
    private String service;

    @Column(name = "fingerprint", nullable = false, updatable = false)
    private String fingerprint;

    @Column(name = "exception_type", nullable = false)
    private String exceptionType;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "stack_trace", columnDefinition = "TEXT")
    private String stackTrace;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severity severity;

    @Column(name = "probable_cause", nullable = false, columnDefinition = "TEXT")
    private String probableCause;

    @Column(name = "suggested_fix", nullable = false, columnDefinition = "TEXT")
    private String suggestedFix;

    @Builder.Default
    @Column(name = "occurrence_count", nullable = false)
    private int occurrenceCount = 1;

    @Column(name = "first_seen", nullable = false, updatable = false)
    private Instant firstSeen;

    @Column(name = "last_seen", nullable = false)
    private Instant lastSeen;
}
