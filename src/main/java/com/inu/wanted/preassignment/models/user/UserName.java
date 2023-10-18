package com.inu.wanted.preassignment.models.user;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class UserName {
    @Column(name = "name")
    private String value;

    private UserName() {

    }

    private UserName(String value) {
        this.value = value;
    }

    public static UserName of(String value) {
        return new UserName(value);
    }
}
