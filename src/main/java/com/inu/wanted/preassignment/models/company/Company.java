package com.inu.wanted.preassignment.models.company;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "companies")
public class Company {
    @EmbeddedId
    private CompanyId id;

    @Embedded
    private CompanyName name;

    @Embedded
    private CompanyLocation location;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public static class Builder {
        private CompanyId id;
        private CompanyName name;
        private CompanyLocation location;

        public Builder id(String id) {
            this.id = new CompanyId(id);
            return this;
        }

        public Builder name(String name) {
            this.name = new CompanyName(name);
            return this;
        }

        public Builder location(String country, String region) {
            this.location = new CompanyLocation(country, region);
            return this;
        }

        public Company build() {
            return new Company(this);
        }
    }

    private Company() {

    }

    public Company(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.location = builder.location;
    }
}
