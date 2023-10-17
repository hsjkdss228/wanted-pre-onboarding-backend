package com.inu.wanted.preassignment.applications;

import com.inu.wanted.preassignment.exceptions.JobOpeningNotFound;
import com.inu.wanted.preassignment.models.jobopening.JobOpeningId;
import com.inu.wanted.preassignment.repositories.JobOpeningRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class DeleteJobOpeningServiceTest {
    private JobOpeningRepository jobOpeningRepository;
    private DeleteJobOpeningService deleteJobOpeningService;

    @BeforeEach
    void setUp() {
        jobOpeningRepository = mock(JobOpeningRepository.class);
        deleteJobOpeningService = new DeleteJobOpeningService(
            jobOpeningRepository
        );
    }

    @Test
    @DisplayName("Success")
    void deleteJobOpening() {
        String jobOpeningId = "JOB_OPENING_UUID";

        given(jobOpeningRepository
            .existsById(JobOpeningId.of(jobOpeningId)))
            .willReturn(true);

        assertDoesNotThrow(() -> deleteJobOpeningService
            .deleteJobOpening(jobOpeningId));

        verify(jobOpeningRepository).deleteById(JobOpeningId.of(jobOpeningId));
    }

    @Test
    @DisplayName("Failure: Throws JobOpeningNotFound")
    void jobOpeningNotFound() {
        String jobOpeningId = "JOB_OPENING_BAD_UUID";

        given(jobOpeningRepository
            .existsById(JobOpeningId.of(jobOpeningId)))
            .willReturn(false);

        assertThrows(JobOpeningNotFound.class, () -> deleteJobOpeningService
            .deleteJobOpening(jobOpeningId));

        verify(jobOpeningRepository, never()).deleteById(any());
    }
}
