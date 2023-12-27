package com.example.uspokajamlekbackend.exercise;

import com.example.uspokajamlekbackend.dailyReport.DailyReport;
import com.example.uspokajamlekbackend.dailyReport.DailyReportService;
import com.example.uspokajamlekbackend.dailyReport.dto.DailyReportRequest;
import com.example.uspokajamlekbackend.user.doctor.Doctor;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@AutoConfigureMockMvc
@SpringBootTest
public class ExerciseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ExerciseService exerciseService;

    @Test
    void shouldAddExercise() throws Exception {
        ExerciseRequest exerciseRequest = ExerciseRequest.builder()
                .category("category")
                .createdBy(1L)
                .description("desc")
                .duration("10")
                .id(1L)
                .build();
        doNothing().when(exerciseService).addExercise(any());
        mockMvc.perform(post("/exercises/add")
                        .content(objectMapper.writeValueAsString(exerciseRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(exerciseService, times(1)).addExercise(any());
    }

    @Test
    void shouldUpdateExercise() throws Exception {
        ExerciseRequest exerciseRequest = ExerciseRequest.builder()
                .category("category")
                .createdBy(1L)
                .description("desc")
                .duration("10")
                .id(1L)
                .build();
        doNothing().when(exerciseService).updateExercise(any());
        mockMvc.perform(put("/exercises")
                        .content(objectMapper.writeValueAsString(exerciseRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(exerciseService, times(1)).updateExercise(any());
    }

    @Test
    void shouldDeleteExercise() throws Exception {
        doNothing().when(exerciseService).deleteExercise(any());
        mockMvc.perform(delete("/exercises/delete?name=name"))
                .andExpect(status().isOk());
        verify(exerciseService, times(1)).deleteExercise("name");
    }

    @Test
    void shouldGetExercises() throws Exception {
        ExerciseResponse exerciseResponse = ExerciseResponse.builder()
                .category("category")
                .createdBy(new Doctor())
                .description("desc")
                .duration("10")
                .id(1L)
                .name("name")
                .build();
        when(exerciseService.getAllExercises()).thenReturn(List.of(exerciseResponse));
        mockMvc.perform(get("/exercises"))
                .andExpect(status().isOk());
        verify(exerciseService, times(1)).getAllExercises();
    }
}
