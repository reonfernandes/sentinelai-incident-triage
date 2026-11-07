package com.reon.sentinelcoreservice.service.impl;

import com.reon.sentinelcoreservice.dto.HashInput;
import com.reon.sentinelcoreservice.service.HashService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Service
@Slf4j
public class HashServiceImpl implements HashService {

    private static final String HASH_ALGORITHM = "SHA-256";
    private static final String SEPARATOR = "|";

    @Override
    public String generateEventHash(HashInput hashInput) {
        log.info("Generating event hash....");

        String raw = String.join(
                SEPARATOR,
                nullToEmpty(hashInput.tenantId()),
                nullToEmpty(hashInput.service()),
                nullToEmpty(hashInput.exceptionType()),
                getFirstLine(hashInput.stackTrace())
        );

        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] bytes = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
            String hash = HexFormat.of().formatHex(bytes);
            log.debug("Generated event hash: {}", hash);
            return hash;
        } catch (NoSuchAlgorithmException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    private String getFirstLine(String stackTrace) {
        if (stackTrace == null || stackTrace.isBlank()) {
            return "";
        }
        return stackTrace.strip().lines().findFirst().orElse("").strip();
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
