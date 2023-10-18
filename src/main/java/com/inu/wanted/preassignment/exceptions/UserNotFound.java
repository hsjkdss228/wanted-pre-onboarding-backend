package com.inu.wanted.preassignment.exceptions;

public class UserNotFound extends RuntimeException {
    public UserNotFound(String userId) {
        super("User Not Found: [userId: " + userId + "]");
    }
}
