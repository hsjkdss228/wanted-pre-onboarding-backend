package com.inu.wanted.preassignment.applications;

import com.inu.wanted.preassignment.dtos.CreateJobOpeningRequestDto;
import com.inu.wanted.preassignment.dtos.CreateJobOpeningResponseDto;
import com.inu.wanted.preassignment.exceptions.CompanyNotFound;
import com.inu.wanted.preassignment.models.company.CompanyId;
import com.inu.wanted.preassignment.models.jobopening.JobOpening;
import com.inu.wanted.preassignment.repositories.JobOpeningRepository;
import com.inu.wanted.preassignment.repositories.CompanyRepository;
import com.inu.wanted.preassignment.utils.UUIDGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class CreateJobOpeningServiceTest {
    private CompanyRepository companyRepository;
    private JobOpeningRepository jobOpeningRepository;
    private UUIDGenerator uuidGenerator;
    private CreateJobOpeningService createJobOpeningService;

    @BeforeEach
    void setUp() {
        companyRepository = mock(CompanyRepository.class);
        jobOpeningRepository = mock(JobOpeningRepository.class);
        uuidGenerator = mock(UUIDGenerator.class);
        createJobOpeningService = new CreateJobOpeningService(
            companyRepository,
            jobOpeningRepository,
            uuidGenerator
        );
    }

    @Test
    @DisplayName("Success")
    void createJobOpening() {
        String companyId = "company_UUID";
        String jobOpeningId = "JOB_OPENING_UUID";

        given(companyRepository.existsById(CompanyId.of(companyId)))
            .willReturn(true);
        given(uuidGenerator.generateRandomJobOpeningUUID())
            .willReturn(jobOpeningId);

        CreateJobOpeningRequestDto createJobOpeningRequestDto
            = CreateJobOpeningRequestDto.builder()
            .companyId(companyId)
            .positionName("QA")
            .rewards(1_000_000L)
            .descriptionBody("QA 포지션입니다.")
            .techStackNames(List.of("JMeter", "Testcontainer"))
            .build();

        CreateJobOpeningResponseDto createJobOpeningResponseDto
            = createJobOpeningService.createJobOpening(createJobOpeningRequestDto);

        assertThat(createJobOpeningResponseDto).isNotNull();
        assertThat(createJobOpeningResponseDto.jobOpeningId()).isEqualTo(jobOpeningId);

        verify(jobOpeningRepository).save(any(JobOpening.class));
    }

    @Test
    @DisplayName("Failure: Throws CompanyNotFound")
    void companyNotFound() {
        String companyId = "COMPANY_BAD_UUID";

        given(companyRepository.existsById(CompanyId.of(companyId)))
            .willReturn(false);

        CreateJobOpeningRequestDto createJobOpeningRequestDto
            = CreateJobOpeningRequestDto.builder()
            .companyId(companyId)
            .build();

        assertThrows(CompanyNotFound.class, () -> createJobOpeningService
            .createJobOpening(createJobOpeningRequestDto));

        verify(uuidGenerator, never()).generateRandomJobOpeningUUID();
        verify(jobOpeningRepository, never()).save(any(JobOpening.class));
    }
}
