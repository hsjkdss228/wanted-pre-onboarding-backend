package com.inu.wanted.preassignment.applications;

import com.inu.wanted.preassignment.exceptions.JobOpeningNotFound;
import com.inu.wanted.preassignment.models.jobopening.JobOpeningId;
import com.inu.wanted.preassignment.repositories.JobOpeningRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeleteJobOpeningService {
    private final JobOpeningRepository jobOpeningRepository;

    public DeleteJobOpeningService(JobOpeningRepository jobOpeningRepository) {
        this.jobOpeningRepository = jobOpeningRepository;
    }

    public void deleteJobOpening(String id) {
        JobOpeningId jobOpeningId = JobOpeningId.of(id);

        if (!jobOpeningRepository.existsById(jobOpeningId)) {
            throw new JobOpeningNotFound(id);
        }

        jobOpeningRepository.deleteById(jobOpeningId);
    }
}
