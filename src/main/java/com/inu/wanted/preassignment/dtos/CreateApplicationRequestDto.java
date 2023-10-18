package com.inu.wanted.preassignment.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateApplicationRequestDto {
    @NotBlank(message = "jobOpeningId Required")
    private String jobOpeningId;

    @NotBlank(message = "userId Required")
    private String userId;
}
