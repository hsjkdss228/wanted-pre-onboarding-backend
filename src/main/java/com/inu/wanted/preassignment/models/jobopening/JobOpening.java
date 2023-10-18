package com.inu.wanted.preassignment.models.jobopening;

import com.inu.wanted.preassignment.dtos.GetJobOpeningDetailResponseDto;
import com.inu.wanted.preassignment.dtos.JobOpeningDetailDto;
import com.inu.wanted.preassignment.dtos.JobOpeningListInDetailDto;
import com.inu.wanted.preassignment.models.Money;
import com.inu.wanted.preassignment.models.TechStack;
import com.inu.wanted.preassignment.models.company.Company;
import com.inu.wanted.preassignment.models.company.CompanyId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "job_openings")
public class JobOpening {
    @EmbeddedId
    private JobOpeningId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "company_id"))
    private CompanyId companyId;

    @Embedded
    private JobOpeningPosition position;

    @Embedded
    private JobOpeningRewards rewards;

    @Embedded
    private JobOpeningDescription description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "job_opening_tech_stacks",
        joinColumns = @JoinColumn(name = "job_opening_id", referencedColumnName = "id")
    )
    private final List<TechStack> techStacks = new ArrayList<>();

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public static class Builder {
        private JobOpeningId id;
        private CompanyId companyId;
        private JobOpeningPosition position;
        private JobOpeningRewards rewards;
        private JobOpeningDescription description;
        private final List<TechStack> techStacks = new ArrayList<>();

        public Builder id(String id) {
            this.id = new JobOpeningId(id);
            return this;
        }

        public Builder companyId(String companyId) {
            this.companyId = new CompanyId(companyId);
            return this;
        }

        public Builder position(String name) {
            this.position = new JobOpeningPosition(name);
            return this;
        }

        public Builder rewards(Long amount) {
            Money money = new Money(amount);
            this.rewards = new JobOpeningRewards(money);
            return this;
        }

        public Builder description(String body) {
            this.description = new JobOpeningDescription(body);
            return this;
        }

        public Builder techStacks(List<String> names) {
            names.forEach(name -> techStacks.add(new TechStack(name)));
            return this;
        }

        public JobOpening build() {
            return new JobOpening(this);
        }
    }

    private JobOpening() {

    }

    public JobOpening(Builder builder) {
        this.id = builder.id;
        this.companyId = builder.companyId;
        this.position = builder.position;
        this.rewards = builder.rewards;
        this.description = builder.description;
        this.techStacks.addAll(builder.techStacks);
    }

    public CompanyId companyId() {
        return companyId;
    }

    public void changePosition(String name) {
        position = new JobOpeningPosition(name);
    }

    public void changeRewards(Long amount) {
        Money money = new Money(amount);
        rewards = new JobOpeningRewards(money);
    }

    public void changeDescription(String body) {
        description = new JobOpeningDescription(body);
    }

    public void changeTechStacks(List<String> names) {
        techStacks.clear();
        names.forEach(name -> techStacks.add(new TechStack(name)));
    }

    public GetJobOpeningDetailResponseDto toDetailResponseDto(
        Company company,
        List<JobOpeningListInDetailDto> otherJobOpenings
    ) {
        List<String> techStackNames = techStacks
            .stream()
            .map(TechStack::name)
            .toList();

        JobOpeningDetailDto jobOpeningDetailDto
            = JobOpeningDetailDto.builder()
            .id(id.value())
            .companyName(company.name())
            .companyCountry(company.country())
            .companyRegion(company.region())
            .positionName(position.name())
            .rewards(rewards.value())
            .techStackNames(techStackNames)
            .descriptionBody(description.body())
            .otherJobOpenings(otherJobOpenings)
            .build();

        return GetJobOpeningDetailResponseDto.builder()
            .jobOpening(jobOpeningDetailDto)
            .build();
    }
}
