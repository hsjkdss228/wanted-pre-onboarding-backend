package com.inu.wanted.preassignment.controllers;

import com.inu.wanted.preassignment.applications.CreateJobOpeningService;
import com.inu.wanted.preassignment.applications.ModifyJobOpeningService;
import com.inu.wanted.preassignment.dtos.CreateJobOpeningRequestDto;
import com.inu.wanted.preassignment.dtos.CreateJobOpeningResponseDto;
import com.inu.wanted.preassignment.exceptions.CompanyNotFound;
import com.inu.wanted.preassignment.exceptions.JobOpeningNotFound;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JobOpeningController.class)
class JobOpeningControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateJobOpeningService createJobOpeningService;

    @MockBean
    private ModifyJobOpeningService modifyJobOpeningService;

    @Nested
    @DisplayName("POST /job-openings")
    class createJobOpening {
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
        @DisplayName("Failure: Blank companyId")
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
                .andExpect(content().string(containsString("companyId")));

            verify(createJobOpeningService, never()).createJobOpening(any());
        }

        @Test
        @DisplayName("Failure: Blank positionName")
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
                .andExpect(content().string(containsString("positionName")));

            verify(createJobOpeningService, never()).createJobOpening(any());
        }

        @Test
        @DisplayName("Failure: Null rewards")
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
                .andExpect(content().string(containsString("rewards")));

            verify(createJobOpeningService, never()).createJobOpening(any());
        }

        @Test
        @DisplayName("Failure: Blank descriptionBody")
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
                .andExpect(content().string(containsString("descriptionBody")));

            verify(createJobOpeningService, never()).createJobOpening(any());
        }

        @Test
        @DisplayName("Failure: Null techStackNames")
        void nullTechStackNames() throws Exception {
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
                .andExpect(content().string(containsString("techStackName")));

            verify(createJobOpeningService, never()).createJobOpening(any());
        }

        @Test
        @DisplayName("Failure: Empty techStackNames")
        void emptyTechStackNames() throws Exception {
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
                .andExpect(content().string(containsString("techStackName")));

            verify(createJobOpeningService, never()).createJobOpening(any());
        }

        @Test
        @DisplayName("Failure: Throws CompanyNotFound")
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

    @Nested
    @DisplayName("PUT /job-openings/{jobOpeningId}")
    class modifyJobOpening {
        @Test
        @DisplayName("Success")
        void modified() throws Exception {
            mockMvc.perform(put("/job-openings/JOB_OPENING_UUID")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                            "positionName": "백엔드 주니어 개발자",
                            "rewards": 1000000,
                            "descriptionBody": "원티드랩에서 백엔드 주니어 개발자를 '적극적으로!' 채용합니다. 자격요건은..",
                            "techStackNames": ["Java", "Kotlin", "Spring Boot", "WebFlux"]
                        }
                        """))
                .andExpect(status().isNoContent());

            verify(modifyJobOpeningService)
                .modifyJobOpening(eq("JOB_OPENING_UUID"), any());
        }

        @Test
        @DisplayName("Failure: Blank positionName")
        void blankPositionName() throws Exception {
            mockMvc.perform(put("/job-openings/JOB_OPENING_UUID")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                            "positionName": null,
                            "rewards": 1000000,
                            "descriptionBody": "원티드랩에서 백엔드 만렙 개발자를 채용합니다. 자격요건은..",
                            "techStackNames": ["Rust", "Swift"]
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("positionName")));

            verify(modifyJobOpeningService, never()).modifyJobOpening(any(), any());
        }

        @Test
        @DisplayName("Failure: Null rewards")
        void nullRewards() throws Exception {
            mockMvc.perform(put("/job-openings/JOB_OPENING_UUID")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                            "positionName": "백엔드 만렙 개발자",
                            "descriptionBody": "원티드랩에서 백엔드 만렙 개발자를 채용합니다. 자격요건은..",
                            "techStackNames": ["Rust", "Swift"]
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("rewards")));

            verify(modifyJobOpeningService, never()).modifyJobOpening(any(), any());
        }

        @Test
        @DisplayName("Failure: Blank descriptionBody")
        void blankDescriptionBody() throws Exception {
            mockMvc.perform(put("/job-openings/JOB_OPENING_UUID")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                            "positionName": "백엔드 만렙 개발자",
                            "rewards": 1000000,
                            "descriptionBody": "              ",
                            "techStackNames": ["Rust", "Swift"]
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("descriptionBody")));

            verify(modifyJobOpeningService, never()).modifyJobOpening(any(), any());
        }

        @Test
        @DisplayName("Failure: Null techStackNames")
        void nullTechStackNames() throws Exception {
            mockMvc.perform(put("/job-openings/JOB_OPENING_UUID")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                            "positionName": "백엔드 만렙 개발자",
                            "rewards": 1000000,
                            "descriptionBody": "원티드랩에서 백엔드 만렙 개발자를 채용합니다. 자격요건은..",
                            "techStackNames": null
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("techStackName")));

            verify(modifyJobOpeningService, never()).modifyJobOpening(any(), any());
        }

        @Test
        @DisplayName("Failure: Empty techStackNames")
        void emptyTechStackNames() throws Exception {
            mockMvc.perform(put("/job-openings/JOB_OPENING_UUID")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                            "positionName": "백엔드 만렙 개발자",
                            "rewards": 1000000,
                            "descriptionBody": "원티드랩에서 백엔드 만렙 개발자를 채용합니다. 자격요건은..",
                            "techStackNames": []
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("techStackName")));

            verify(modifyJobOpeningService, never()).modifyJobOpening(any(), any());
        }

        @Test
        @DisplayName("Failure: Throws JobOpeningNotFound")
        void jobOpeningNotFound() throws Exception {
            doThrow(JobOpeningNotFound.class)
                .when(modifyJobOpeningService).modifyJobOpening(any(), any());

            mockMvc.perform(put("/job-openings/JOB_OPENING_UUID")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                            "positionName": "백엔드 만렙 개발자",
                            "rewards": 1000000,
                            "descriptionBody": "원티드랩에서 백엔드 만렙 개발자를 채용합니다. 자격요건은..",
                            "techStackNames": ["Rust", "Swift"]
                        }
                        """))
                .andExpect(status().isNotFound());
        }
    }
}
