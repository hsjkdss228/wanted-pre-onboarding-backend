package com.inu.wanted.preassignment.models.application;

import com.inu.wanted.preassignment.models.jobopening.JobOpening;
import com.inu.wanted.preassignment.models.jobopening.JobOpeningId;
import com.inu.wanted.preassignment.models.user.User;
import com.inu.wanted.preassignment.models.user.UserId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
public class Application {
    @EmbeddedId
    private ApplicationId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "job_opening_id"))
    private JobOpeningId jobOpeningId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "user_id"))
    private UserId userId;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public static class Builder {
        private ApplicationId id;
        private JobOpeningId jobOpeningId;
        private UserId userId;

        public Builder id(String id) {
            this.id = new ApplicationId(id);
            return this;
        }

        public Builder jobOpening(JobOpening jobOpening) {
            this.jobOpeningId = jobOpening.id();
            return this;
        }

        public Builder user(User user) {
            this.userId = user.id();
            return this;
        }

        public Application build() {
            return new Application(this);
        }
    }

    private Application() {

    }

    public Application(Builder builder) {
        this.id = builder.id;
        this.jobOpeningId = builder.jobOpeningId;
        this.userId = builder.userId;
    }
}
