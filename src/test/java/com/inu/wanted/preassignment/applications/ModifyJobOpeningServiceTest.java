package com.inu.wanted.preassignment.applications;

import com.inu.wanted.preassignment.dtos.ModifyJobOpeningRequestDto;
import com.inu.wanted.preassignment.exceptions.JobOpeningNotFound;
import com.inu.wanted.preassignment.models.jobopening.JobOpening;
import com.inu.wanted.preassignment.models.jobopening.JobOpeningId;
import com.inu.wanted.preassignment.repositories.JobOpeningRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class ModifyJobOpeningServiceTest {
    private JobOpeningRepository jobOpeningRepository;
    private ModifyJobOpeningService modifyJobOpeningService;

    @BeforeEach
    void setUp() {
        jobOpeningRepository = mock(JobOpeningRepository.class);
        modifyJobOpeningService = new ModifyJobOpeningService(
            jobOpeningRepository
        );
    }

    @Test
    @DisplayName("Success")
    void modifyJobOpening() {
        String jobOpeningId = "JOB_OPENING_UUID";

        JobOpening jobOpening = spy(new JobOpening.Builder()
            .id(jobOpeningId)
            .companyId("COMPANY_UUID")
            .position("CTO")
            .rewards(20_000_000L)
            .description("기술 혁신을 이끌어주실 CTO님을 모십니다.")
            .techStacks(List.of("Slack", "Jira", "Confluence"))
            .build()
        );
        given(jobOpeningRepository.findById(JobOpeningId.of(jobOpeningId)))
            .willReturn(Optional.of(jobOpening));

        ModifyJobOpeningRequestDto modifyJobOpeningRequestDto
            = ModifyJobOpeningRequestDto.builder()
            .positionName("CEO")
            .rewards(200_000_000L)
            .descriptionBody("회사를 뿌리부터 바꿔주실 CEO님을 모십니다.")
            .techStackNames(List.of("판단력", "의사결정 능력", "운"))
            .build();

        assertDoesNotThrow(() -> modifyJobOpeningService
            .modifyJobOpening(jobOpeningId, modifyJobOpeningRequestDto));

        verify(jobOpening)
            .changePosition(modifyJobOpeningRequestDto.getPositionName());
        verify(jobOpening)
            .changeRewards(modifyJobOpeningRequestDto.getRewards());
        verify(jobOpening)
            .changeDescription(modifyJobOpeningRequestDto.getDescriptionBody());
        verify(jobOpening)
            .changeTechStacks(modifyJobOpeningRequestDto.getTechStackNames());
    }

    @Test
    @DisplayName("Failure: Throws JobOpeningNotFound")
    void jobOpeningNotFound() {
        String jobOpeningId = "JOB_OPENING_BAD_UUID";

        given(jobOpeningRepository
            .findById(JobOpeningId.of(jobOpeningId)))
            .willThrow(JobOpeningNotFound.class);

        ModifyJobOpeningRequestDto modifyJobOpeningRequestDto
            = ModifyJobOpeningRequestDto.builder()
                .build();

        assertThrows(JobOpeningNotFound.class, () -> modifyJobOpeningService
            .modifyJobOpening(jobOpeningId, modifyJobOpeningRequestDto));
    }
}