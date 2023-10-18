package com.inu.wanted.preassignment.models.user;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
public class UserId implements Serializable {
    @Column(name = "id")
    private String value;

    private UserId() {

    }

    private UserId(String value) {
        this.value = value;
    }

    public static UserId of(String value) {
        return new UserId(value);
    }
}
