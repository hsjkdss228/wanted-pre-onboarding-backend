package com.inu.wanted.preassignment.applications;

import com.inu.wanted.preassignment.dtos.ModifyJobOpeningRequestDto;
import com.inu.wanted.preassignment.exceptions.JobOpeningNotFound;
import com.inu.wanted.preassignment.models.jobopening.JobOpening;
import com.inu.wanted.preassignment.models.jobopening.JobOpeningId;
import com.inu.wanted.preassignment.repositories.JobOpeningRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ModifyJobOpeningService {
    private final JobOpeningRepository jobOpeningRepository;

    public ModifyJobOpeningService(JobOpeningRepository jobOpeningRepository) {
        this.jobOpeningRepository = jobOpeningRepository;
    }

    public void modifyJobOpening(
        String jobOpeningId,
        ModifyJobOpeningRequestDto modifyJobOpeningRequestDto
    ) {
        JobOpening jobOpeningExisting = jobOpeningRepository
            .findById(new JobOpeningId(jobOpeningId))
            .orElseThrow(() -> new JobOpeningNotFound(jobOpeningId));

        String positionName = modifyJobOpeningRequestDto.getPositionName();
        Long rewards = modifyJobOpeningRequestDto.getRewards();
        String descriptionBody = modifyJobOpeningRequestDto.getDescriptionBody();
        List<String> techStackNames = modifyJobOpeningRequestDto.getTechStackNames();

        jobOpeningExisting.changePosition(positionName);
        jobOpeningExisting.changeRewards(rewards);
        jobOpeningExisting.changeDescription(descriptionBody);
        jobOpeningExisting.changeTechStacks(techStackNames);
    }
}
