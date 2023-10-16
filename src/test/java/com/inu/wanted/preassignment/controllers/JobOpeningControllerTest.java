package com.inu.wanted.preassignment.controllers;

import com.inu.wanted.preassignment.applications.CreateJobOpeningService;
import com.inu.wanted.preassignment.dtos.CreateJobOpeningRequestDto;
import com.inu.wanted.preassignment.dtos.CreateJobOpeningResponseDto;
import com.inu.wanted.preassignment.exceptions.CompanyNotFound;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JobOpeningController.class)
class JobOpeningControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateJobOpeningService createJobOpeningService;

    @Nested
    @DisplayName("POST /job-openings")
    class create {
        @Test
        @DisplayName("Success")
        void created() throws Exception {
            CreateJobOpeningResponseDto createJobOpeningResponseDto
                = CreateJobOpeningResponseDto.builder()
                .jobOpeningId("jobOpening_UUID")
                .build();

            given(createJobOpeningService
                .createJobOpening(any(CreateJobOpeningRequestDto.class)))
                .willReturn(createJobOpeningResponseDto);

            mockMvc.perform(post("/job-openings")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                            "companyId": "COMPANY_UUID",
                            "positionName": "백엔드 주니어 개발자",
                            "rewards": 1000000,
                            "descriptionBody": "원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..",
                            "techStackNames": ["Java", "Spring Boot"]
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("jobOpening_UUID")));
        }

        @Test
        @DisplayName("Failure: Blank Company Id")
        void blankCompanyId() throws Exception {
            mockMvc.perform(post("/job-openings")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                            "companyId": "",
                            "positionName": "백엔드 주니어 개발자",
                            "rewards": 1000000,
                            "descriptionBody": "원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..",
                            "techStackNames": ["Java", "Spring Boot"]
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Company Id")));

            verify(createJobOpeningService, never()).createJobOpening(any());
        }

        @Test
        @DisplayName("Failure: Blank Position Name")
        void blankPositionName() throws Exception {
            mockMvc.perform(post("/job-openings")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                            "companyId": "COMPANY_UUID",
                            "positionName": "",
                            "rewards": 1000000,
                            "descriptionBody": "원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..",
                            "techStackNames": ["Java", "Spring Boot"]
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Position Name")));

            verify(createJobOpeningService, never()).createJobOpening(any());
        }

        @Test
        @DisplayName("Failure: Null Rewards")
        void nullRewards() throws Exception {
            mockMvc.perform(post("/job-openings")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                            "companyId": "COMPANY_UUID",
                            "positionName": "프론트엔드 시니어 개발자",
                            "rewards": null,
                            "descriptionBody": "원티드랩에서 프론트엔드 시니어 개발자를 채용합니다. 자격요건은..",
                            "techStackNames": ["TypeScript", "React"]
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Rewards")));

            verify(createJobOpeningService, never()).createJobOpening(any());
        }

        @Test
        @DisplayName("Failure: Blank Description Body")
        void blankDescriptionBody() throws Exception {
            mockMvc.perform(post("/job-openings")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                            "companyId": "COMPANY_UUID",
                            "positionName": "백엔드 주니어 개발자",
                            "rewards": 1000000,
                            "descriptionBody": "",
                            "techStackNames": ["Java", "Spring Boot"]
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Description Body")));

            verify(createJobOpeningService, never()).createJobOpening(any());
        }

        @Test
        @DisplayName("Failure: Null Tech Stack")
        void nullTechStacks() throws Exception {
            mockMvc.perform(post("/job-openings")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                            "companyId": "COMPANY_UUID",
                            "positionName": "백엔드 주니어 개발자",
                            "rewards": 1000000,
                            "descriptionBody": "원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..",
                            "techStackNames": null
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Tech Stack")));

            verify(createJobOpeningService, never()).createJobOpening(any());
        }

        @Test
        @DisplayName("Failure: Empty Tech Stack")
        void emptyTechStacks() throws Exception {
            mockMvc.perform(post("/job-openings")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                            "companyId": "COMPANY_UUID",
                            "positionName": "백엔드 주니어 개발자",
                            "rewards": 1000000,
                            "descriptionBody": "원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..",
                            "techStackNames": []
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Tech Stack")));

            verify(createJobOpeningService, never()).createJobOpening(any());
        }

        @Test
        @DisplayName("Failure: Company Not Found")
        void companyNotFound() throws Exception {
            given(createJobOpeningService
                .createJobOpening(any(CreateJobOpeningRequestDto.class)))
                .willThrow(CompanyNotFound.class);

            mockMvc.perform(post("/job-openings")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                            "companyId": "COMPANY_BAD_UUID",
                            "positionName": "백엔드 주니어 개발자",
                            "rewards": 1000000,
                            "descriptionBody": "원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..",
                            "techStackNames": ["Java", "Spring Boot"]
                        }
                        """))
                .andExpect(status().isNotFound());
        }
    }
}
