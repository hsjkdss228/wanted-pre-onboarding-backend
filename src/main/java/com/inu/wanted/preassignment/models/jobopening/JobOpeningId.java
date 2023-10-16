package com.inu.wanted.preassignment.models.jobopening;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
public class JobOpeningId implements Serializable {
    @Column(name = "id")
    private String value;

    private JobOpeningId() {

    }

    public JobOpeningId(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
