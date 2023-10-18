package com.inu.wanted.preassignment.dtos;

import lombok.Builder;

@Builder
public record JobOpeningListInDetailDto(
    String id,
    String positionName
) {

}
