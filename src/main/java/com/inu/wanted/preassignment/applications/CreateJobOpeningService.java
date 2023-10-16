package com.inu.wanted.preassignment.applications;

import com.inu.wanted.preassignment.dtos.CreateJobOpeningRequestDto;
import com.inu.wanted.preassignment.dtos.CreateJobOpeningResponseDto;
import com.inu.wanted.preassignment.exceptions.CompanyNotFound;
import com.inu.wanted.preassignment.models.company.CompanyId;
import com.inu.wanted.preassignment.models.jobopening.JobOpening;
import com.inu.wanted.preassignment.repositories.JobOpeningRepository;
import com.inu.wanted.preassignment.repositories.CompanyRepository;
import com.inu.wanted.preassignment.utils.UUIDGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CreateJobOpeningService {
    private final CompanyRepository companyRepository;
    private final JobOpeningRepository jobOpeningRepository;
    private final UUIDGenerator uuidGenerator;

    public CreateJobOpeningService(CompanyRepository companyRepository,
                                   JobOpeningRepository jobOpeningRepository,
                                   UUIDGenerator uuidGenerator) {
        this.companyRepository = companyRepository;
        this.uuidGenerator = uuidGenerator;
        this.jobOpeningRepository = jobOpeningRepository;
    }

    public CreateJobOpeningResponseDto createJobOpening(
        CreateJobOpeningRequestDto createJobOpeningRequestDto
    ) {
        String companyId = createJobOpeningRequestDto.getCompanyId();

        if (!companyRepository.existsById(CompanyId.of(companyId))) {
            throw new CompanyNotFound(companyId);
        }

        String jobOpeningId = uuidGenerator.generateRandomJobOpeningUUID();
        String positionName = createJobOpeningRequestDto.getPositionName();
        Long rewards = createJobOpeningRequestDto.getRewards();
        String descriptionBody = createJobOpeningRequestDto.getDescriptionBody();
        List<String> techStackNames = createJobOpeningRequestDto.getTechStackNames();

        JobOpening jobOpening = new JobOpening.Builder()
            .id(jobOpeningId)
            .companyId(companyId)
            .position(positionName)
            .rewards(rewards)
            .description(descriptionBody)
            .techStacks(techStackNames)
            .build();

        jobOpeningRepository.save(jobOpening);

        return new CreateJobOpeningResponseDto(jobOpeningId);
    }
}
