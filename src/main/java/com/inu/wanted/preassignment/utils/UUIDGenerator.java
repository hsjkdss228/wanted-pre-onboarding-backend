package com.inu.wanted.preassignment.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDGenerator {
    public String generateRandomJobOpeningUUID() {
        return "JOB_OPENING_" + UUID.randomUUID();
    }

    public String generateRandomApplicationUUID() {
        return "APPLICATION_" + UUID.randomUUID();
    }
}
