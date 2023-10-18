package com.inu.wanted.preassignment.models.company;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class CompanyLocation {
    @Column(name = "country")
    private String country;

    @Column(name = "region")
    private String region;

    private CompanyLocation() {

    }

    public CompanyLocation(String country, String region) {
        this.country = country;
        this.region = region;
    }

    public String country() {
        return country;
    }

    public String region() {
        return region;
    }
}
