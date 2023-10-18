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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ApplyJobOpeningService {
    private final JobOpeningRepository jobOpeningRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final UUIDGenerator uuidGenerator;

    public ApplyJobOpeningService(JobOpeningRepository jobOpeningRepository,
                                  UserRepository userRepository,
                                  ApplicationRepository applicationRepository,
                                  UUIDGenerator uuidGenerator) {
        this.userRepository = userRepository;
        this.jobOpeningRepository = jobOpeningRepository;
        this.applicationRepository = applicationRepository;
        this.uuidGenerator = uuidGenerator;
    }

    public CreateApplicationResponseDto apply(
        CreateApplicationRequestDto createApplicationRequestDto
    ) {
        String jobOpeningId = createApplicationRequestDto.getJobOpeningId();

        JobOpening jobOpening = jobOpeningRepository
            .findById(JobOpeningId.of(jobOpeningId))
            .orElseThrow(() -> new JobOpeningNotFound(jobOpeningId));

        String userId = createApplicationRequestDto.getUserId();

        User user = userRepository
            .findById(UserId.of(userId))
            .orElseThrow(() -> new UserNotFound(userId));

        if (applicationRepository.existsByJobOpeningIdAndUserId(
            JobOpeningId.of(jobOpeningId),
            UserId.of(userId)
        )) {
            throw new AlreadyAppliedJobOpening(jobOpeningId, userId);
        }

        String applicationId = uuidGenerator.generateRandomApplicationUUID();

        Application application = new Application.Builder()
            .id(applicationId)
            .jobOpening(jobOpening)
            .user(user)
            .build();

        applicationRepository.save(application);

        return CreateApplicationResponseDto.builder()
            .applicationId(applicationId)
            .build();
    }
}
