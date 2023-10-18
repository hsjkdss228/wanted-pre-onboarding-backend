package com.inu.wanted.preassignment.models.application;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
public class ApplicationId implements Serializable {
    @Column(name = "id")
    private String value;

    private ApplicationId() {

    }

    public ApplicationId(String value) {
        this.value = value;
    }

    public static ApplicationId of(String value) {
        return new ApplicationId(value);
    }
}
