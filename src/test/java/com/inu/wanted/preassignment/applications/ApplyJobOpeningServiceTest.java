package com.inu.wanted.preassignment.applications;

import com.inu.wanted.preassignment.dtos.CreateApplicationRequestDto;
import com.inu.wanted.preassignment.dtos.CreateApplicationResponseDto;
import com.inu.wanted.preassignment.exceptions.AlreadyAppliedJobOpening;
import com.inu.wanted.preassignment.exceptions.JobOpeningNotFound;
import com.inu.wanted.preassignment.exceptions.UserNotFound;
import com.inu.wanted.preassignment.models.application.Application;
import com.inu.wanted.preassignment.models.jobopening.JobOpening;
import com.inu.wanted.preassignment.models.jobopening.JobOpeningId;
import com.inu.wanted.preassignment.models.user.User;
import com.inu.wanted.preassignment.models.user.UserId;
import com.inu.wanted.preassignment.repositories.ApplicationRepository;
import com.inu.wanted.preassignment.repositories.JobOpeningRepository;
import com.inu.wanted.preassignment.repositories.UserRepository;
import com.inu.wanted.preassignment.utils.UUIDGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class ApplyJobOpeningServiceTest {
    private JobOpeningRepository jobOpeningRepository;
    private UserRepository userRepository;
    private ApplicationRepository applicationRepository;
    private UUIDGenerator uuidGenerator;
    private ApplyJobOpeningService applyJobOpeningService;

    @BeforeEach
    void setUp() {
        jobOpeningRepository = mock(JobOpeningRepository.class);
        userRepository = mock(UserRepository.class);
        applicationRepository = mock(ApplicationRepository.class);
        uuidGenerator = mock(UUIDGenerator.class);
        applyJobOpeningService = new ApplyJobOpeningService(
            jobOpeningRepository,
            userRepository,
            applicationRepository,
            uuidGenerator
        );
    }

    @Test
    @DisplayName("Success")
    void apply() {
        String jobOpeningId = "JOB_OPENING_UUID";

        JobOpening jobOpening = new JobOpening.Builder()
            .id(jobOpeningId)
            .companyId("COMPANY_UUID")
            .position("Manager")
            .rewards(100L)
            .description("Gonaryja")
            .techStacks(List.of("Eloquence", "Wit"))
            .build();

        given(jobOpeningRepository
            .findById(JobOpeningId.of(jobOpeningId)))
            .willReturn(Optional.of(jobOpening));

        String userId = "USER_UUID";

        User user = new User(userId, "Hwang Inwoo");

        given(userRepository
            .findById(UserId.of(userId)))
            .willReturn(Optional.of(user));

        given(applicationRepository.existsByJobOpeningIdAndUserId(
            JobOpeningId.of(jobOpeningId),
            UserId.of(userId)
        )).willReturn(false);

        given(uuidGenerator
            .generateRandomApplicationUUID())
            .willReturn("APPLICATION_UUID");

        CreateApplicationRequestDto createApplicationRequestDto
            = CreateApplicationRequestDto.builder()
            .jobOpeningId(jobOpeningId)
            .userId(userId)
            .build();

        CreateApplicationResponseDto createApplicationResponseDto
            = applyJobOpeningService.apply(createApplicationRequestDto);
        assertThat(createApplicationResponseDto).isNotNull();

        assertThat(createApplicationResponseDto.applicationId())
            .isEqualTo("APPLICATION_UUID");

        verify(applicationRepository).save(any(Application.class));
    }

    @Test
    @DisplayName("Failed: Throws JobOpeningNotFound")
    void jobOpeningNotFound() {
        String jobOpeningId = "JOB_OPENING_BAD_UUID";

        given(jobOpeningRepository
            .findById(JobOpeningId.of(jobOpeningId)))
            .willThrow(JobOpeningNotFound.class);

        CreateApplicationRequestDto createApplicationRequestDto
            = CreateApplicationRequestDto.builder()
            .jobOpeningId(jobOpeningId)
            .build();

        assertThrows(JobOpeningNotFound.class, () -> applyJobOpeningService
            .apply(createApplicationRequestDto));

        verify(applicationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Failed: Throws UserNotFound")
    void userNotFound() {
        String jobOpeningId = "JOB_OPENING_UUID";

        JobOpening jobOpening = new JobOpening.Builder()
            .id(jobOpeningId)
            .companyId("COMPANY_UUID")
            .position("Artist")
            .rewards(500L)
            .description("Mona Lisa")
            .techStacks(List.of("Sense"))
            .build();

        given(jobOpeningRepository
            .findById(JobOpeningId.of(jobOpeningId)))
            .willReturn(Optional.of(jobOpening));

        String userId = "USER_BAD_UUID";

        given(jobOpeningRepository
            .findById(JobOpeningId.of(jobOpeningId)))
            .willThrow(UserNotFound.class);

        CreateApplicationRequestDto createApplicationRequestDto
            = CreateApplicationRequestDto.builder()
            .jobOpeningId(jobOpeningId)
            .userId(userId)
            .build();

        assertThrows(UserNotFound.class, () -> applyJobOpeningService
            .apply(createApplicationRequestDto));

        verify(applicationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Failed: Throws AlreadyAppliedJobOpening")
    void alreadyAppliedJobOpening() {
        String jobOpeningId = "JOB_OPENING_UUID";

        JobOpening jobOpening = new JobOpening.Builder()
            .id(jobOpeningId)
            .companyId("COMPANY_UUID")
            .position("Cucumber")
            .rewards(100L)
            .description("delicious")
            .techStacks(List.of("Water"))
            .build();

        given(jobOpeningRepository
            .findById(JobOpeningId.of(jobOpeningId)))
            .willReturn(Optional.of(jobOpening));

        String userId = "USER_UUID";

        User user = new User(userId, "Horse");

        given(userRepository
            .findById(UserId.of(userId)))
            .willReturn(Optional.of(user));

        given(applicationRepository.existsByJobOpeningIdAndUserId(
            JobOpeningId.of(jobOpeningId),
            UserId.of(userId)
        )).willReturn(true);

        CreateApplicationRequestDto createApplicationRequestDto
            = CreateApplicationRequestDto.builder()
            .jobOpeningId(jobOpeningId)
            .userId(userId)
            .build();

        assertThrows(AlreadyAppliedJobOpening.class, () -> applyJobOpeningService
            .apply(createApplicationRequestDto));

        verify(applicationRepository, never()).save(any());
    }
}
