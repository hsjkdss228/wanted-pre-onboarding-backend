package com.inu.wanted.preassignment.exceptions;

public class JobOpeningNotFound extends RuntimeException {
    public JobOpeningNotFound(String jobOpeningId) {
        super("JobOpening Not Found [jobOpeningId: " + jobOpeningId + "]");
    }
}
