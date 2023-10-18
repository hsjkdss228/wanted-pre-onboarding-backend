package com.inu.wanted.preassignment.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateJobOpeningRequestDto {
    @NotBlank(message = "companyId Required")
    private String companyId;

    @NotBlank(message = "positionName Required")
    private String positionName;

    @NotNull(message = "rewards Required")
    private Long rewards;

    @NotBlank(message = "descriptionBody Required")
    private String descriptionBody;

    @NotEmpty(message = "At Least 1 techStackName Required")
    private List<String> techStackNames;
}
