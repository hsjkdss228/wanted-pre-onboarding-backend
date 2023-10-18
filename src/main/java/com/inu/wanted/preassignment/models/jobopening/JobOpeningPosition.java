package com.inu.wanted.preassignment.models.jobopening;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class JobOpeningPosition {
    @Column(name = "position_name")
    private String name;

    private JobOpeningPosition() {

    }

    public JobOpeningPosition(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }
}
