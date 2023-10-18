package com.inu.wanted.preassignment.repositories;

import com.inu.wanted.preassignment.dtos.GetJobOpeningsResponseDto;
import com.inu.wanted.preassignment.dtos.JobOpeningListInDetailDto;
import com.inu.wanted.preassignment.models.company.CompanyId;
import com.inu.wanted.preassignment.models.jobopening.JobOpeningId;

import java.util.List;

public interface JobOpeningQueryDslRepository {
    GetJobOpeningsResponseDto findAllJobOpenings(String keyword);

    List<JobOpeningListInDetailDto> findAllOtherJobOpenings(
        JobOpeningId jobOpeningId,
        CompanyId companyId
    );
}
