package com.inu.wanted.preassignment.repositories;

import com.inu.wanted.preassignment.dtos.GetJobOpeningsResponseDto;

public interface JobOpeningQueryDslRepository {
    GetJobOpeningsResponseDto findAllJobOpenings(String keyword);
}
