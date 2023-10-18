package com.inu.wanted.preassignment.applications;

import com.inu.wanted.preassignment.dtos.GetJobOpeningDetailResponseDto;
import com.inu.wanted.preassignment.dtos.JobOpeningListInDetailDto;
import com.inu.wanted.preassignment.exceptions.CompanyNotFound;
import com.inu.wanted.preassignment.exceptions.JobOpeningNotFound;
import com.inu.wanted.preassignment.models.company.Company;
import com.inu.wanted.preassignment.models.company.CompanyId;
import com.inu.wanted.preassignment.models.jobopening.JobOpening;
import com.inu.wanted.preassignment.models.jobopening.JobOpeningId;
import com.inu.wanted.preassignment.repositories.CompanyRepository;
import com.inu.wanted.preassignment.repositories.JobOpeningRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GetJobOpeningDetailService {
    private final JobOpeningRepository jobOpeningRepository;
    private final CompanyRepository companyRepository;

    public GetJobOpeningDetailService(JobOpeningRepository jobOpeningRepository,
                                      CompanyRepository companyRepository) {
        this.jobOpeningRepository = jobOpeningRepository;
        this.companyRepository = companyRepository;
    }

    public GetJobOpeningDetailResponseDto getJobOpeningDetail(
        String jobOpeningIdRaw
    ) {
        JobOpeningId jobOpeningId = JobOpeningId.of(jobOpeningIdRaw);

        JobOpening jobOpening = jobOpeningRepository
            .findById(jobOpeningId)
            .orElseThrow(() -> new JobOpeningNotFound(jobOpeningIdRaw));

        CompanyId companyId = jobOpening.companyId();

        Company company = companyRepository
            .findById(companyId)
            .orElseThrow(() -> new CompanyNotFound(companyId.value()));

        List<JobOpeningListInDetailDto> otherJobOpenings
            = jobOpeningRepository.findAllOtherJobOpenings(jobOpeningId, companyId);

        return jobOpening.toDetailResponseDto(company, otherJobOpenings);
    }
}
