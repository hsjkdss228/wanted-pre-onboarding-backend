package com.inu.wanted.preassignment.exceptions;

public class CompanyNotFound extends RuntimeException {
    public CompanyNotFound(String companyId) {
        super("Company Not Found [companyId: " + companyId + "]");
    }
}
