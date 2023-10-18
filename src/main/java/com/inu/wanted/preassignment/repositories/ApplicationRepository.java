package com.inu.wanted.preassignment.repositories;

import com.inu.wanted.preassignment.models.application.Application;
import com.inu.wanted.preassignment.models.application.ApplicationId;
import com.inu.wanted.preassignment.models.jobopening.JobOpeningId;
import com.inu.wanted.preassignment.models.user.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, ApplicationId> {
    boolean existsByJobOpeningIdAndUserId(
        JobOpeningId jobOpeningId,
        UserId userId
    );
}
