package com.inu.wanted.preassignment.dtos;

import lombok.Builder;

@Builder
public record CreateApplicationResponseDto(
    String applicationId
) {

}
