package com.inu.wanted.preassignment.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
public class CreateJobOpeningRequestDto {
    @NotBlank(message = "Company Id Required")
    private String companyId;

    @NotBlank(message = "Position Name Required")
    private String positionName;

    @NotNull(message = "Rewards Required")
    private Long rewards;

    @NotBlank(message = "Description Body Required")
    private String descriptionBody;

    @NotEmpty(message = "At Least 1 Tech Stack Required")
    private List<String> techStackNames;

    private CreateJobOpeningRequestDto() {

    }
}
