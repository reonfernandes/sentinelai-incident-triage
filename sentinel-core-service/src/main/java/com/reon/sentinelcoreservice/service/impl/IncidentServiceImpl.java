package com.reon.sentinelcoreservice.service.impl;

import com.reon.dto.AiDiagnosis;
import com.reon.dto.RawLogEvent;
import com.reon.sentinelcoreservice.dto.response.IncidentResponse;
import com.reon.sentinelcoreservice.entity.Incident;
import com.reon.sentinelcoreservice.mapper.IncidentMapper;
import com.reon.sentinelcoreservice.repository.IncidentRepository;
import com.reon.sentinelcoreservice.service.IncidentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;

@Service
@Slf4j
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository incidentRepository;
    private final IncidentMapper incidentMapper;
    private final Clock clock;

    public IncidentServiceImpl(IncidentRepository incidentRepository, IncidentMapper incidentMapper, Clock clock) {
        this.incidentRepository = incidentRepository;
        this.incidentMapper = incidentMapper;
        this.clock = clock;
    }

    @Override
    public IncidentResponse saveNewIncident(RawLogEvent logEvent, AiDiagnosis diagnosis, String fingerprint) {
        log.info("Incident saving in progress..");

        Incident incident = incidentMapper.mapToEntity(logEvent, diagnosis, fingerprint);
        incident.setOccurrenceCount(1);
        incident.setFirstSeen(Instant.now(clock));
        incident.setLastSeen(Instant.now(clock));
        incidentRepository.save(incident);

        log.info("New incident saved successfully...");
        return incidentMapper.responseToUser(incident);
    }
}
