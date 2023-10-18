package com.inu.wanted.preassignment.models.jobopening;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class JobOpeningDescription {
    @Column(name = "description_body")
    private String body;

    private JobOpeningDescription() {

    }

    public JobOpeningDescription(String body) {
        this.body = body;
    }

    public String body() {
        return body;
    }
}
