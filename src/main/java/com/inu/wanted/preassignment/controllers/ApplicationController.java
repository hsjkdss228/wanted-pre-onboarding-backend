package com.inu.wanted.preassignment.controllers;

import com.inu.wanted.preassignment.applications.ApplyJobOpeningService;
import com.inu.wanted.preassignment.dtos.CreateApplicationRequestDto;
import com.inu.wanted.preassignment.dtos.CreateApplicationResponseDto;
import com.inu.wanted.preassignment.exceptions.AlreadyAppliedJobOpening;
import com.inu.wanted.preassignment.exceptions.InvalidApplicationInputs;
import com.inu.wanted.preassignment.exceptions.JobOpeningNotFound;
import com.inu.wanted.preassignment.exceptions.UserNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("applications")
public class ApplicationController {
    private final ApplyJobOpeningService applyJobOpeningService;

    public ApplicationController(ApplyJobOpeningService applyJobOpeningService) {
        this.applyJobOpeningService = applyJobOpeningService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateApplicationResponseDto createApplication(
        @Validated @RequestBody CreateApplicationRequestDto createApplicationRequestDto,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getAllErrors()
                .get(0)
                .getDefaultMessage();
            throw new InvalidApplicationInputs(message);
        }

        return applyJobOpeningService.apply(createApplicationRequestDto);
    }

    @ExceptionHandler(InvalidApplicationInputs.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String invalidApplicationInputs(RuntimeException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(JobOpeningNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String jobOpeningNotFound(RuntimeException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(UserNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String userNotFound(RuntimeException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(AlreadyAppliedJobOpening.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String alreadyAppliedJobOpening(RuntimeException exception) {
        return exception.getMessage();
    }
}
