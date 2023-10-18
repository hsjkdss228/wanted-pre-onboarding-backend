package com.inu.wanted.preassignment.dtos;

import lombok.Builder;

import java.util.List;

@Builder
public record JobOpeningListDto(
    String id,
    String companyName,
    String companyCountry,
    String companyRegion,
    String positionName,
    Long rewards,
    List<String> techStackNames
) {

}
