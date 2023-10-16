package com.inu.wanted.preassignment.models.company;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
public class CompanyId implements Serializable {
    @Column(name = "id")
    private String value;

    private CompanyId() {

    }

    public CompanyId(String value) {
        this.value = value;
    }

    public static CompanyId of(String companyId) {
        return new CompanyId(companyId);
    }

    public String value() {
        return value;
    }
}
