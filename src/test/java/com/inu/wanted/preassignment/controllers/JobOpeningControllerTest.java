package com.inu.wanted.preassignment.controllers;

import com.inu.wanted.preassignment.applications.CreateJobOpeningService;
import com.inu.wanted.preassignment.applications.DeleteJobOpeningService;
import com.inu.wanted.preassignment.applications.GetJobOpeningDetailService;
import com.inu.wanted.preassignment.applications.ModifyJobOpeningService;
import com.inu.wanted.preassignment.dtos.CreateJobOpeningRequestDto;
import com.inu.wanted.preassignment.dtos.CreateJobOpeningResponseDto;
import com.inu.wanted.preassignment.dtos.GetJobOpeningDetailResponseDto;
import com.inu.wanted.preassignment.dtos.GetJobOpeningsResponseDto;
import com.inu.wanted.preassignment.dtos.JobOpeningDetailDto;
import com.inu.wanted.preassignment.dtos.JobOpeningListDto;
import com.inu.wanted.preassignment.exceptions.CompanyNotFound;
import com.inu.wanted.preassignment.exceptions.JobOpeningNotFound;
import com.inu.wanted.preassignment.repositories.JobOpeningRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JobOpeningController.class)
class JobOpeningControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetJobOpeningDetailService getJobOpeningDetailService;

    @MockBean
    private CreateJobOpeningService createJobOpeningService;

    @MockBean
    private ModifyJobOpeningService modifyJobOpeningService;

    @MockBean
    private DeleteJobOpeningService deleteJobOpeningService;

    @MockBean
    private JobOpeningRepository jobOpeningRepository;

    @Nested
    @DisplayName("GET /job-openings")
    class getJobOpenings {
        @Nested
        @DisplayName("Without query parameters")
        class withoutQueryParameters {
            @Test
            @DisplayName("Get List of JobOpenings")
            void getList() throws Exception {
                List<JobOpeningListDto> jobOpeningListDtos = List.of(
                    JobOpeningListDto.builder()
                        .id("JOB_OPENING_UUID_1")
                        .companyName("Wanted Lab")
                        .companyCountry("South Korea")
                        .companyRegion("Seoul")
                        .positionName("Junior Backend Developer")
                        .rewards(1_000_000L)
                        .techStackNames(List.of("Python", "Django"))
                        .build(),
                    JobOpeningListDto.builder()
                        .id("JOB_OPENING_UUID_2")
                        .companyName("Very Huge Dinosaur Company")
                        .companyCountry("USA")
                        .companyRegion("New York City")
                        .positionName("Majestic Developer")
                        .rewards(1_000_000_000_000L)
                        .techStackNames(List.of("Power"))
                        .build()
                );
                GetJobOpeningsResponseDto getJobOpeningsResponseDto
                    = new GetJobOpeningsResponseDto(jobOpeningListDtos);

                String keyword = null;

                given(jobOpeningRepository.findAllJobOpenings(keyword))
                    .willReturn(getJobOpeningsResponseDto);

                mockMvc.perform(get("/job-openings"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("JOB_OPENING_UUID_1")))
                    .andExpect(content().string(containsString("JOB_OPENING_UUID_2")));
            }

            @Test
            @DisplayName("Get empty List")
            void getEmptyList() throws Exception {
                List<JobOpeningListDto> jobOpeningListDtos = List.of();
                GetJobOpeningsResponseDto getJobOpeningsResponseDto
                    = new GetJobOpeningsResponseDto(jobOpeningListDtos);

                String keyword = null;

                given(jobOpeningRepository.findAllJobOpenings(keyword))
                    .willReturn(getJobOpeningsResponseDto);

                mockMvc.perform(get("/job-openings"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("[]")));
            }
        }

        @Nested
        @DisplayName("With query parameters")
        class withQueryParameters {
            @Test
            @DisplayName("Get search result List of JobOpenings")
            void getList() throws Exception {
                List<JobOpeningListDto> jobOpeningListDtos = List.of(
                    JobOpeningListDto.builder()
                        .id("JOB_OPENING_UUID_2")
                        .companyName("Very Huge Dinosaur Company")
                        .companyCountry("USA")
                        .companyRegion("New York City")
                        .positionName("Majestic Developer")
                        .rewards(1_000_000_000_000L)
                        .techStackNames(List.of("Power"))
                        .build()
                );
                GetJobOpeningsResponseDto getJobOpeningsResponseDto
                    = new GetJobOpeningsResponseDto(jobOpeningListDtos);

                String keyword = "Dinosaur";

                given(jobOpeningRepository.findAllJobOpenings(keyword))
                    .willReturn(getJobOpeningsResponseDto);

                mockMvc.perform(get("/job-openings")
                        .param("keyword", keyword))
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("JOB_OPENING_UUID_2")));
            }
        }
    }

    @Nested
    @DisplayName("GET /job-openings/{jobOpeningId}")
    class getJobOpening {
        @Test
        @DisplayName("Success")
        void created() throws Exception {
            String jobOpeningId = "JOB_OPENING_UUID";

            JobOpeningDetailDto jobOpeningDetailDto
                = JobOpeningDetailDto.builder()
                .id(jobOpeningId)
                .companyName("Oceania Aviation")
                .companyCountry("Australia")
                .companyRegion("Brisbane")
                .positionName("Flight Crew")
                .rewards(2_000_000L)
                .techStackNames(List.of("English", "Chinese"))
                .descriptionBody("We're hiring for a friendly Flight Crew.")
                .otherJobOpenings(List.of())
                .build();

            GetJobOpeningDetailResponseDto getJobOpeningDetailResponseDto
                = GetJobOpeningDetailResponseDto.builder()
                .jobOpening(jobOpeningDetailDto)
                .build();

            given(getJobOpeningDetailService.getJobOpeningDetail(jobOpeningId))
                .willReturn(getJobOpeningDetailResponseDto);

            mockMvc.perform(get("/job-openings/JOB_OPENING_UUID"))
                .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Failed: Throws JobOpeningNotFound")
        void jobOpeningNotFound() throws Exception {
            given(getJobOpeningDetailService.getJobOpeningDetail(any()))
                .willThrow(JobOpeningNotFound.class);

            mockMvc.perform(get("/job-openings/JOB_OPENING_UUID"))
                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Failed: Throws CompanyNotFound")
        void companyNotFound() throws Exception {
            given(getJobOpeningDetailService.getJobOpeningDetail(any()))
                .willThrow(CompanyNotFound.class);

            mockMvc.perform(get("/job-openings/JOB_OPENING_UUID"))
                .andExpect(status().isNotFound());
        }
    }

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

    @Nested
    @DisplayName("DELETE /job-openings/{jobOpeningId}")
    class deleteJobOpening {
        @Test
        @DisplayName("Success")
        void deleted() throws Exception {
            mockMvc.perform(delete("/job-openings/JOB_OPENING_UUID"))
                .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Failure: Throws JobOpeningNotFound")
        void jobOpeningNotFound() throws Exception {
            doThrow(JobOpeningNotFound.class)
                .when(deleteJobOpeningService).deleteJobOpening(any());

            mockMvc.perform(delete("/job-openings/JOB_OPENING_UUID"))
                .andExpect(status().isNotFound());
        }
    }
}
