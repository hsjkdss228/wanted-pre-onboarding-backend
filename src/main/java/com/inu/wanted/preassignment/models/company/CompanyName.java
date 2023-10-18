package com.inu.wanted.preassignment.models.company;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class CompanyName {
    @Column(name = "name")
    private String value;

    private CompanyName() {

    }

    public CompanyName(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
