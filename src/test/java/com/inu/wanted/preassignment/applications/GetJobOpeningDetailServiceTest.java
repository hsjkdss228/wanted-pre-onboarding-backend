package com.inu.wanted.preassignment.applications;

import com.inu.wanted.preassignment.dtos.GetJobOpeningDetailResponseDto;
import com.inu.wanted.preassignment.dtos.JobOpeningDetailDto;
import com.inu.wanted.preassignment.exceptions.CompanyNotFound;
import com.inu.wanted.preassignment.exceptions.JobOpeningNotFound;
import com.inu.wanted.preassignment.models.company.Company;
import com.inu.wanted.preassignment.models.company.CompanyId;
import com.inu.wanted.preassignment.models.jobopening.JobOpening;
import com.inu.wanted.preassignment.models.jobopening.JobOpeningId;
import com.inu.wanted.preassignment.repositories.CompanyRepository;
import com.inu.wanted.preassignment.repositories.JobOpeningRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GetJobOpeningDetailServiceTest {
    private JobOpeningRepository jobOpeningRepository;
    private CompanyRepository companyRepository;
    private GetJobOpeningDetailService getJobOpeningDetailService;

    @BeforeEach
    void setUp() {
        jobOpeningRepository = mock(JobOpeningRepository.class);
        companyRepository = mock(CompanyRepository.class);
        getJobOpeningDetailService = new GetJobOpeningDetailService(
            jobOpeningRepository,
            companyRepository
        );
    }

    @Test
    @DisplayName("Success")
    void getJobOpeningDetail() {
        String jobOpeningId = "JOB_OPENING_UUID";
        String companyId = "COMPANY_UUID";

        JobOpening jobOpening = new JobOpening.Builder()
            .id(jobOpeningId)
            .companyId(companyId)
            .position("Developer")
            .rewards(2_222_220L)
            .description("abcdefghijklmnopqrstuvwxyz")
            .techStacks(List.of("COBOL", "FORTRAN"))
            .build();

        given(jobOpeningRepository
            .findById(JobOpeningId.of(jobOpeningId)))
            .willReturn(Optional.of(jobOpening));

        Company company = new Company.Builder()
            .id(companyId)
            .name("Extremely Mysterious Company")
            .location("Solar System", "Jupiter")
            .build();

        given(companyRepository
            .findById(CompanyId.of(companyId)))
            .willReturn(Optional.of(company));

        given(jobOpeningRepository
            .findAllOtherJobOpenings(
                JobOpeningId.of(jobOpeningId),
                CompanyId.of(companyId)
            ))
            .willReturn(List.of());

        GetJobOpeningDetailResponseDto getJobOpeningDetailResponseDto
            = getJobOpeningDetailService.getJobOpeningDetail(jobOpeningId);
        assertThat(getJobOpeningDetailResponseDto).isNotNull();

        JobOpeningDetailDto jobOpeningDetailDto
            = getJobOpeningDetailResponseDto.jobOpening();
        assertThat(jobOpeningDetailDto).isNotNull();

        assertThat(jobOpeningDetailDto.id()).isEqualTo(jobOpeningId);
    }

    @Test
    @DisplayName("Failed: Throws JobOpeningNotFound")
    void jobOpeningNotFound() {
        String jobOpeningId = "JOB_OPENING_BAD_UUID";
        String companyId = "COMPANY_UUID";

        given(jobOpeningRepository
            .findById(JobOpeningId.of(jobOpeningId)))
            .willThrow(JobOpeningNotFound.class);

        assertThrows(JobOpeningNotFound.class, () -> getJobOpeningDetailService
            .getJobOpeningDetail(jobOpeningId));
    }

    @Test
    @DisplayName("Failed: Throws CompanyNotFound")
    void companyNotFound() {
        String jobOpeningId = "JOB_OPENING_UUID";
        String companyId = "COMPANY_BAD_UUID";

        JobOpening jobOpening = new JobOpening.Builder()
            .id(jobOpeningId)
            .companyId(companyId)
            .position("Angel")
            .rewards(2_222_220L)
            .description("Blessings to the brave!")
            .techStacks(List.of("Mercy"))
            .build();

        given(jobOpeningRepository
            .findById(JobOpeningId.of(jobOpeningId)))
            .willReturn(Optional.of(jobOpening));

        given(companyRepository
            .findById(CompanyId.of(companyId)))
            .willThrow(CompanyNotFound.class);

        assertThrows(CompanyNotFound.class, () -> getJobOpeningDetailService
            .getJobOpeningDetail(jobOpeningId));
    }
}
