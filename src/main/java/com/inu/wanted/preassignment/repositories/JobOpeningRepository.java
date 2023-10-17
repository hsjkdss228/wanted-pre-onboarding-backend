package com.inu.wanted.preassignment.repositories;

import com.inu.wanted.preassignment.models.jobopening.JobOpening;
import com.inu.wanted.preassignment.models.jobopening.JobOpeningId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobOpeningRepository
    extends JpaRepository<JobOpening, JobOpeningId>,
    JobOpeningQueryDslRepository {

}
