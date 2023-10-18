package com.inu.wanted.preassignment.repositories;

import com.inu.wanted.preassignment.applications.ApplyJobOpeningService;
import com.inu.wanted.preassignment.controllers.ApplicationController;
import com.inu.wanted.preassignment.dtos.CreateApplicationRequestDto;
import com.inu.wanted.preassignment.dtos.CreateApplicationResponseDto;
import com.inu.wanted.preassignment.exceptions.AlreadyAppliedJobOpening;
import com.inu.wanted.preassignment.exceptions.JobOpeningNotFound;
import com.inu.wanted.preassignment.exceptions.UserNotFound;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApplicationController.class)
class ApplicationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplyJobOpeningService applyJobOpeningService;

    @Nested
    @DisplayName("POST /applications")
    class createApplication {
        @Test
        @DisplayName("Success")
        void created() throws Exception {
            CreateApplicationResponseDto createApplicationResponseDto
                = CreateApplicationResponseDto.builder()
                .applicationId("APPLICATION_UUID")
                .build();

            given(applyJobOpeningService
                .apply(any(CreateApplicationRequestDto.class)))
                .willReturn(createApplicationResponseDto);

            mockMvc.perform(post("/applications")
                    .contentType("application/json")
                    .content("""
                    {
                        "jobOpeningId": "JOB_OPENING_UUID",
                        "userId": "USER_UUID"
                    }
                    """))
                .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Failure: Blank jobOpeningId")
        void blankJobOpeningId() throws Exception {
            mockMvc.perform(post("/applications")
                    .contentType("application/json")
                    .content("""
                    {
                        "jobOpeningId": "   ",
                        "userId": "USER_UUID"
                    }
                    """))
                .andExpect(status().isBadRequest());

            verify(applyJobOpeningService, never()).apply(any());
        }

        @Test
        @DisplayName("Failure: Blank userId")
        void blankUserId() throws Exception {
            mockMvc.perform(post("/applications")
                    .contentType("application/json")
                    .content("""
                    {
                        "jobOpeningId": "JOB_OPENING_UUID",
                        "userId": null
                    }
                    """))
                .andExpect(status().isBadRequest());

            verify(applyJobOpeningService, never()).apply(any());
        }

        @Test
        @DisplayName("Failure: Throws JobOpeningNotFound")
        void jobOpeningNotFound() throws Exception {
            given(applyJobOpeningService
                .apply(any(CreateApplicationRequestDto.class)))
                .willThrow(JobOpeningNotFound.class);

            mockMvc.perform(post("/applications")
                .contentType("application/json")
                .content("""
                    {
                        "jobOpeningId": "JOB_OPENING_BAD_UUID",
                        "userId": "USER_UUID"
                    }
                    """))
                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Failure: Throws UserNotFound")
        void userNotFound() throws Exception {
            given(applyJobOpeningService
                .apply(any(CreateApplicationRequestDto.class)))
                .willThrow(UserNotFound.class);

            mockMvc.perform(post("/applications")
                    .contentType("application/json")
                    .content("""
                    {
                        "jobOpeningId": "JOB_OPENING_BAD_UUID",
                        "userId": "USER_UUID"
                    }
                    """))
                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Failure: Throws AlreadyAppliedJobOpening")
        void alreadyAppliedJobOpening() throws Exception {
            given(applyJobOpeningService
                .apply(any(CreateApplicationRequestDto.class)))
                .willThrow(AlreadyAppliedJobOpening.class);

            mockMvc.perform(post("/applications")
                    .contentType("application/json")
                    .content("""
                    {
                        "jobOpeningId": "JOB_OPENING_UUID",
                        "userId": "USER_UUID"
                    }
                    """))
                .andExpect(status().isConflict());
        }
    }
}
