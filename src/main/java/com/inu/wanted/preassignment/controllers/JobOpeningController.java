package com.inu.wanted.preassignment.controllers;

import com.inu.wanted.preassignment.applications.CreateJobOpeningService;
import com.inu.wanted.preassignment.dtos.CreateJobOpeningRequestDto;
import com.inu.wanted.preassignment.dtos.CreateJobOpeningResponseDto;
import com.inu.wanted.preassignment.exceptions.CompanyNotFound;
import com.inu.wanted.preassignment.exceptions.InvalidJobOpeningInputs;
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
@RequestMapping("job-openings")
public class JobOpeningController {
    private final CreateJobOpeningService createJobOpeningService;

    public JobOpeningController(CreateJobOpeningService createJobOpeningService) {
        this.createJobOpeningService = createJobOpeningService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateJobOpeningResponseDto create(
        @Validated @RequestBody CreateJobOpeningRequestDto createJobOpeningRequestDto,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getAllErrors()
                .get(0)
                .getDefaultMessage();
            throw new InvalidJobOpeningInputs(message);
        }

        return createJobOpeningService.createJobOpening(createJobOpeningRequestDto);
    }

    @ExceptionHandler(InvalidJobOpeningInputs.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String invalidJobOpeningInputs(RuntimeException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(CompanyNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String companyNotFound(RuntimeException exception) {
        return exception.getMessage();
    }
}
