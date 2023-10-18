package com.inu.wanted.preassignment.repositories;

import com.inu.wanted.preassignment.dtos.GetJobOpeningsResponseDto;
import com.inu.wanted.preassignment.dtos.JobOpeningListDto;
import com.inu.wanted.preassignment.models.TechStack;
import com.inu.wanted.preassignment.models.jobopening.JobOpeningId;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.inu.wanted.preassignment.models.company.QCompany.company;
import static com.inu.wanted.preassignment.models.jobopening.QJobOpening.jobOpening;

@Repository
public class JobOpeningQueryDslRepositoryImpl implements
    JobOpeningQueryDslRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public JobOpeningQueryDslRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public GetJobOpeningsResponseDto findAllJobOpenings(String keyword) {
        JPAQuery<Tuple> query = jpaQueryFactory
            .select(
                jobOpening.id.value,
                company.name.value,
                company.location.country,
                company.location.region,
                jobOpening.position.name,
                jobOpening.rewards.rewards
            )
            .from(jobOpening)
            .innerJoin(company).on(company.id.eq(jobOpening.companyId));

        if (keyword != null) {
            query = filter(query, keyword);
        }

        List<Tuple> jobOpeningListDtosWithoutTechStackNames = queryResult(query);

        Map<String, List<String>> jobOpeningIdTechStackNamesPairs
            = pair(jobOpeningListDtosWithoutTechStackNames);

        List<JobOpeningListDto> jobOpeningListDtos = toJobOpeningListDtos(
            jobOpeningListDtosWithoutTechStackNames,
            jobOpeningIdTechStackNamesPairs
        );

        return new GetJobOpeningsResponseDto(jobOpeningListDtos);
    }

    private JPAQuery<Tuple> filter(JPAQuery<Tuple> query, String keyword) {
        TechStack techStack = TechStack.of(keyword);

        return query
            .where(company.name.value.like("%" + keyword + "%")
                .or(company.location.country.like("%" + keyword + "%"))
                .or(company.location.region.like("%" + keyword + "%"))
                .or(jobOpening.position.name.like("%" + keyword + "%"))
                .or(jobOpening.techStacks.contains(techStack))
            );
    }

    private List<Tuple> queryResult(JPAQuery<Tuple> query) {
        return query
            .orderBy(jobOpening.createdAt.desc())
            .fetch();
    }

    private Map<String, List<String>> pair(
        List<Tuple> jobOpeningListDtosWithoutTechStackNames
    ) {
        Map<String, List<String>> jobOpeningIdTechStackNamesPairs = new HashMap<>();

        List<JobOpeningId> jobOpeningIds = jobOpeningListDtosWithoutTechStackNames
            .stream()
            .map(tuple -> JobOpeningId.of(tuple.get(jobOpening.id.value)))
            .toList();

        List<Tuple> jobOpeningIdAndTechStacks
            = jpaQueryFactory
            .select(
                jobOpening.id.value,
                jobOpening.techStacks
            )
            .from(jobOpening)
            .where(jobOpening.id.in(jobOpeningIds))
            .fetch();

        jobOpeningIdAndTechStacks.forEach(jobOpeningIdAndTechStack -> {
            String jobOpeningId = jobOpeningIdAndTechStack
                .get(jobOpening.id.value);
            String techStackName = ((TechStack) jobOpeningIdAndTechStack
                .get(jobOpening.techStacks))
                .name();

            if (!jobOpeningIdTechStackNamesPairs.containsKey(jobOpeningId)) {
                List<String> techStackNames = new ArrayList<>();
                techStackNames.add(techStackName);
                jobOpeningIdTechStackNamesPairs.put(jobOpeningId, techStackNames);
                return;
            }

            jobOpeningIdTechStackNamesPairs.get(jobOpeningId).add(techStackName);
        });

        return jobOpeningIdTechStackNamesPairs;
    }

    private List<JobOpeningListDto> toJobOpeningListDtos(
        List<Tuple> jobOpeningListDtosWithoutTechStackNames,
        Map<String, List<String>> jobOpeningIdTechStackNamesPairs
    ) {
        return jobOpeningListDtosWithoutTechStackNames
            .stream()
            .map(tuple -> {
                Long rewards = tuple
                    .get(jobOpening.rewards.rewards)
                    .amount();
                List<String> techStackNames = jobOpeningIdTechStackNamesPairs
                    .get(tuple.get(jobOpening.id.value));

                return JobOpeningListDto.builder()
                    .id(tuple.get(jobOpening.id.value))
                    .companyName(tuple.get(company.name.value))
                    .companyCountry(tuple.get(company.location.country))
                    .companyRegion(tuple.get(company.location.region))
                    .positionName(tuple.get(jobOpening.position.name))
                    .rewards(rewards)
                    .techStackNames(techStackNames)
                    .build();
            })
            .toList();
    }
}
