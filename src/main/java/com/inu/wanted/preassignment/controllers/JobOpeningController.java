package com.inu.wanted.preassignment.controllers;

import com.inu.wanted.preassignment.applications.CreateJobOpeningService;
import com.inu.wanted.preassignment.applications.DeleteJobOpeningService;
import com.inu.wanted.preassignment.applications.ModifyJobOpeningService;
import com.inu.wanted.preassignment.dtos.CreateJobOpeningRequestDto;
import com.inu.wanted.preassignment.dtos.CreateJobOpeningResponseDto;
import com.inu.wanted.preassignment.dtos.GetJobOpeningsResponseDto;
import com.inu.wanted.preassignment.dtos.ModifyJobOpeningRequestDto;
import com.inu.wanted.preassignment.exceptions.CompanyNotFound;
import com.inu.wanted.preassignment.exceptions.InvalidJobOpeningInputs;
import com.inu.wanted.preassignment.exceptions.JobOpeningNotFound;
import com.inu.wanted.preassignment.repositories.JobOpeningRepository;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("job-openings")
public class JobOpeningController {
    private final CreateJobOpeningService createJobOpeningService;
    private final ModifyJobOpeningService modifyJobOpeningService;
    private final DeleteJobOpeningService deleteJobOpeningService;
    private final JobOpeningRepository jobOpeningRepository;

    public JobOpeningController(CreateJobOpeningService createJobOpeningService,
                                ModifyJobOpeningService modifyJobOpeningService,
                                DeleteJobOpeningService deleteJobOpeningService,
                                JobOpeningRepository jobOpeningRepository) {
        this.createJobOpeningService = createJobOpeningService;
        this.modifyJobOpeningService = modifyJobOpeningService;
        this.deleteJobOpeningService = deleteJobOpeningService;
        this.jobOpeningRepository = jobOpeningRepository;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public GetJobOpeningsResponseDto jobOpenings() {
        return jobOpeningRepository.findAllJobOpenings();
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

    @PutMapping("{jobOpeningId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modify(
        @PathVariable String jobOpeningId,
        @Validated @RequestBody ModifyJobOpeningRequestDto modifyJobOpeningRequestDto,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            String message = bindingResult.getAllErrors()
                .get(0)
                .getDefaultMessage();
            throw new InvalidJobOpeningInputs(message);
        }

        modifyJobOpeningService.modifyJobOpening(jobOpeningId, modifyJobOpeningRequestDto);
    }

    @DeleteMapping("{jobOpeningId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String jobOpeningId) {
        deleteJobOpeningService.deleteJobOpening(jobOpeningId);
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

    @ExceptionHandler(JobOpeningNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String jobOpeningNotFound(RuntimeException exception) {
        return exception.getMessage();
    }
}
