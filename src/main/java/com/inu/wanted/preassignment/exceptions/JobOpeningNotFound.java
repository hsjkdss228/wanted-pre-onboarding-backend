package com.inu.wanted.preassignment.exceptions;

public class JobOpeningNotFound extends RuntimeException {
    public JobOpeningNotFound(String jobOpeningId) {
        super("Job Opening Not Found [jobOpeningId: " + jobOpeningId + "]");
    }
}
