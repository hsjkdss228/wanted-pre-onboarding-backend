package com.inu.wanted.preassignment.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UUIDGeneratorTest {
    private UUIDGenerator uuidGenerator;

    @BeforeEach
    void setUp() {
        uuidGenerator = new UUIDGenerator();
    }

    @Test
    @DisplayName("Generate Random Job Opening UUID")
    void generateRandomJobOpeningUUID() {
        String jobOpeningId = uuidGenerator.generateRandomJobOpeningUUID();

        assertThat(jobOpeningId).startsWith("JOB_OPENING_");
    }
}
