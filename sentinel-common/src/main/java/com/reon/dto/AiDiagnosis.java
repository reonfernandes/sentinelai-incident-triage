package com.reon.dto;

public record AiDiagnosis(
        Severity severity,
        String probableCause,
        String suggestedFix
) {}