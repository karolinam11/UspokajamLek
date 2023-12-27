package com.example.uspokajamlekbackend.dailyReport;

import com.example.uspokajamlekbackend.assignedExercise.AssignedExerciseService;
import com.example.uspokajamlekbackend.assignedExercise.dto.AssignExerciseRequest;
import com.example.uspokajamlekbackend.dailyReport.dto.DailyReportRequest;
import com.example.uspokajamlekbackend.dailyReport.dto.MoodsRequest;
import com.example.uspokajamlekbackend.user.patient.Patient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@AutoConfigureMockMvc
@SpringBootTest
public class DailyReportControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DailyReportService dailyReportService;

    @Test
    void shouldGetDailyReports() throws Exception {
        DailyReportRequest dailyReportRequest = DailyReportRequest.builder()
                .mood("mood")
                .note("note")
                .date(LocalDate.now())
                .build();
        DailyReport dailyReport = DailyReport.builder()
                .mood("mood")
                .note("note")
                .date(LocalDate.now())
                .patientDailyReport(new Patient())
                .build();
        when(dailyReportService.getUserDailyReports(any())).thenReturn(List.of(dailyReport));
        mockMvc.perform(get("/daily-reports?id=1"))
                .andExpect(status().isOk());
        verify(dailyReportService, times(1)).getUserDailyReports(any());
    }

    @Test
    void shouldCheckIfDailyReportCanBeAdded() throws Exception {
        when(dailyReportService.checkIfDailyReportCanBeAdded(any(), any())).thenReturn(true);

        mockMvc.perform(get("/daily-reports/today"))
                .andExpect(status().isOk());
        verify(dailyReportService, times(1)).checkIfDailyReportCanBeAdded(any(), any());
    }

    @Test
    void shouldAddActivity() throws Exception {
        DailyReportRequest dailyReportRequest = DailyReportRequest.builder()
                .mood("mood")
                .note("note")
                .date(LocalDate.now())
                .build();

        when(dailyReportService.checkIfDailyReportCanBeAdded(any(), any())).thenReturn(true);
        doNothing().when(dailyReportService).addDailyReport(any(), any());
        mockMvc.perform(post("/daily-reports/add")
                        .content(objectMapper.writeValueAsString(dailyReportRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(dailyReportService, times(1)).addDailyReport(any(), any());
    }

    @Test
    void shouldNotAddActivity() throws Exception {
        DailyReportRequest dailyReportRequest = DailyReportRequest.builder()
                .mood("mood")
                .note("note")
                .date(LocalDate.now())
                .build();

        when(dailyReportService.checkIfDailyReportCanBeAdded(any(), any())).thenReturn(false);
        mockMvc.perform(post("/daily-reports/add")
                        .content(objectMapper.writeValueAsString(dailyReportRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetMoods() throws Exception {
        MoodsRequest moodsRequest = MoodsRequest.builder()
                .userId(1L)
                .numOfDays(7)
                .build();
        Mood mood = Mood.builder()
                .date(LocalDate.of(2023,10,10))
                .name("name")
                .build();
        when(dailyReportService.getMoods(anyInt(), anyLong())).thenReturn(List.of(mood));
        mockMvc.perform(post("/daily-reports/moods")
                        .content(objectMapper.writeValueAsString(moodsRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date").exists())
                .andExpect(jsonPath("$[0].name").exists());
    }

    @Test
    void shouldGetMoodsQuantity() throws Exception {
        when(dailyReportService.getMoodsQuantityInLastXDays(anyLong(), anyInt())).thenReturn(List.of(1,2,3,4,5));
        mockMvc.perform(get("/daily-reports/moods-quantity?userId=1&days=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[1]").exists())
                .andExpect(jsonPath("$[2]").exists())
                .andExpect(jsonPath("$[3]").exists())
                .andExpect(jsonPath("$[4]").exists());
    }

    @Test
    void shouldGetLongestStreak() throws Exception {
        when(dailyReportService.getLongestStreak(anyLong())).thenReturn(10);
        mockMvc.perform(get("/daily-reports/longest-streak?userId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void shouldGetCurrentStreak() throws Exception {
        when(dailyReportService.getCurrentStreak(anyLong())).thenReturn(10);
        mockMvc.perform(get("/daily-reports/current-streak?userId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void shouldDeleteReport() throws Exception {
        doNothing().when(dailyReportService).removeReport(anyLong());
        mockMvc.perform(delete("/daily-reports/delete?id=1"))
                .andExpect(status().isOk());
    }
}