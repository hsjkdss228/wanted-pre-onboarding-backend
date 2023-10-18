package com.inu.wanted.preassignment.exceptions;

public class AlreadyAppliedJobOpening extends RuntimeException {
    public AlreadyAppliedJobOpening(String jobOpeningId, String userId) {
        super("Already Applied JobOpening: " +
            "[jobOpeningId: " + jobOpeningId + ", " +
            "userId: " + userId + "]");
    }
}
