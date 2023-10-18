package com.inu.wanted.preassignment.dtos;

import java.util.List;

public record GetJobOpeningsResponseDto(
    List<JobOpeningListDto> jobOpenings
) {

}
