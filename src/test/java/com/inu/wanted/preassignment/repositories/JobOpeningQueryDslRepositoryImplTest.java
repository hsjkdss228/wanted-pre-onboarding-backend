package com.inu.wanted.preassignment.repositories;

import com.inu.wanted.preassignment.config.QueryDslTestConfig;
import com.inu.wanted.preassignment.dtos.GetJobOpeningsResponseDto;
import com.inu.wanted.preassignment.dtos.JobOpeningListDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QueryDslTestConfig.class)
@ActiveProfiles("test")
class JobOpeningQueryDslRepositoryImplTest {
    @Autowired
    private JobOpeningQueryDslRepositoryImpl repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM job_opening_tech_stacks");
        jdbcTemplate.execute("DELETE FROM job_openings");
        jdbcTemplate.execute("DELETE FROM companies");

        jdbcTemplate.update("""
                INSERT INTO companies(id, name, country, region, created_at)
                VALUES (?, ?, ?, ?, ?),
                (?, ?, ?, ?, ?)
                """,
            "COMPANY_UUID_1", "company1", "country1", "region1", LocalDateTime.now(),
            "COMPANY_UUID_2", "company2", "country2", "region2", LocalDateTime.now().plusHours(1)
        );
        jdbcTemplate.update("""
                INSERT INTO job_openings(id, company_id, position_name, rewards, description_body, created_at)
                VALUES (?, ?, ?, ?, ?, ?),
                (?, ?, ?, ?, ?, ?),
                (?, ?, ?, ?, ?, ?),
                (?, ?, ?, ?, ?, ?)
                """,
            "JOB_OPENING_UUID_1", "COMPANY_UUID_1", "position1", 1_000_000L, "description", LocalDateTime.now(),
            "JOB_OPENING_UUID_2", "COMPANY_UUID_1", "position1", 1_000_000L, "description", LocalDateTime.now().plusHours(3),
            "JOB_OPENING_UUID_3", "COMPANY_UUID_2", "position2", 1_000_000L, "description", LocalDateTime.now().plusHours(1),
            "JOB_OPENING_UUID_4", "COMPANY_UUID_2", "position2", 1_000_000L, "description", LocalDateTime.now().plusHours(2)
        );
        jdbcTemplate.update("""
                INSERT INTO job_opening_tech_stacks(job_opening_id, name)
                VALUES (?, ?),
                (?, ?),
                (?, ?),
                (?, ?),
                (?, ?)
                """,
            "JOB_OPENING_UUID_1", "tech1",
            "JOB_OPENING_UUID_1", "tech2",
            "JOB_OPENING_UUID_2", "tech3",
            "JOB_OPENING_UUID_3", "tech1",
            "JOB_OPENING_UUID_4", "tech1"
        );
    }

    @Nested
    @DisplayName("Without keyword")
    class withoutKeyword {
        @Test
        @DisplayName("Query all of JobOpeningListDtos in List order by createdAt descendent")
        void findAllJobOpenings() {
            String keyword = null;

            GetJobOpeningsResponseDto getJobOpeningsResponseDto = repository
                .findAllJobOpenings(keyword);
            assertThat(getJobOpeningsResponseDto).isNotNull();

            List<JobOpeningListDto> jobOpeningListDtos = getJobOpeningsResponseDto
                .jobOpenings();
            assertThat(jobOpeningListDtos).hasSize(4);
            assertThat(jobOpeningListDtos.get(0).id()).isEqualTo("JOB_OPENING_UUID_2");
            assertThat(jobOpeningListDtos.get(1).id()).isEqualTo("JOB_OPENING_UUID_4");
            assertThat(jobOpeningListDtos.get(2).id()).isEqualTo("JOB_OPENING_UUID_3");
            assertThat(jobOpeningListDtos.get(3).id()).isEqualTo("JOB_OPENING_UUID_1");
        }
    }

    @Nested
    @DisplayName("With keyword")
    class withKeyword {
        @Nested
        @DisplayName("When contains in TechStacks")
        class whenContainsKeyword {
            @Test
            @DisplayName("Query targeted JobOpeningListDtos in List order by createdAt descendent")
            void findTargetedJobOpenings() {
                String keyword = "tech1";

                GetJobOpeningsResponseDto getJobOpeningsResponseDto = repository
                    .findAllJobOpenings(keyword);
                assertThat(getJobOpeningsResponseDto).isNotNull();

                List<JobOpeningListDto> jobOpeningListDtos = getJobOpeningsResponseDto
                    .jobOpenings();
                assertThat(jobOpeningListDtos).hasSize(3);

                List<String> jobOpeningIds = List.of(
                      "JOB_OPENING_UUID_1",
                      "JOB_OPENING_UUID_3",
                      "JOB_OPENING_UUID_4"
                );
                jobOpeningListDtos.forEach(jobOpeningListDto -> {
                    assertThat(jobOpeningIds).contains(jobOpeningListDto.id());
                });
            }
        }
    }
}
