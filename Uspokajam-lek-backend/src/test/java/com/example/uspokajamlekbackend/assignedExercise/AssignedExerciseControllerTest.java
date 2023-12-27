package com.example.uspokajamlekbackend.assignedExercise;

import com.example.uspokajamlekbackend.appointment.AppointmentResponse;
import com.example.uspokajamlekbackend.appointment.AppointmentService;
import com.example.uspokajamlekbackend.assignedExercise.dto.AssignExerciseRequest;
import com.example.uspokajamlekbackend.assignedExercise.dto.AssignedExerciseResponse;
import com.example.uspokajamlekbackend.exercise.ExerciseResponse;
import com.example.uspokajamlekbackend.user.doctor.dto.DoctorResponse;
import com.example.uspokajamlekbackend.user.patient.dto.PatientResponse;
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
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@AutoConfigureMockMvc
@SpringBootTest
public class AssignedExerciseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AssignedExerciseService assignedExerciseService;

    @Test
    void shouldAssignExercise() throws Exception {
        AssignExerciseRequest assignExerciseRequest = AssignExerciseRequest.builder()
                .exerciseId(1L)
                .dueDate(LocalDate.of(2024,10,10))
                .doctorId(1L)
                .patientId(1L)
                .build();
        doNothing().when(assignedExerciseService).assignExercise(any());
        mockMvc.perform(post("/assign-exercise")
                        .content(objectMapper.writeValueAsString(assignExerciseRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(assignedExerciseService, times(1)).assignExercise(any());
    }

    @Test
    void shouldGetPatientAssignedExercises() throws Exception {
        AssignedExerciseResponse assignedExerciseResponse = AssignedExerciseResponse.builder()
                .id(1L)
                .assignedBy(new DoctorResponse())
                .assignedTo(new PatientResponse())
                .exercise(new ExerciseResponse())
                .isDone(true)
                .dueDate(LocalDate.of(2024,10,10))
                .build();
        when(assignedExerciseService.getUserAssignedExercises(1L))
                .thenReturn(List.of(assignedExerciseResponse));
        mockMvc.perform(get("/patient-assigned-exercises?id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].assignedBy").exists())
                .andExpect(jsonPath("$[0].assignedTo").exists())
                .andExpect(jsonPath("$[0].exercise").exists())
                .andExpect(jsonPath("$[0].done").exists())
                .andExpect(jsonPath("$[0].dueDate").exists());
        verify(assignedExerciseService, times(1)).getUserAssignedExercises(any());
    }

    @Test
    void shouldGetNewPatientAssignedExercises() throws Exception {
        AssignedExerciseResponse assignedExerciseResponse = AssignedExerciseResponse.builder()
                .id(1L)
                .assignedBy(new DoctorResponse())
                .assignedTo(new PatientResponse())
                .exercise(new ExerciseResponse())
                .isDone(true)
                .dueDate(LocalDate.of(2024,10,10))
                .build();
        when(assignedExerciseService.getNewUserAssignedExercises(1L)).thenReturn(List.of(assignedExerciseResponse));
        mockMvc.perform(get("/patient-new-assigned-exercises?id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].assignedBy").exists())
                .andExpect(jsonPath("$[0].assignedTo").exists())
                .andExpect(jsonPath("$[0].exercise").exists())
                .andExpect(jsonPath("$[0].done").exists())
                .andExpect(jsonPath("$[0].dueDate").exists());
        verify(assignedExerciseService, times(1)).getNewUserAssignedExercises(any());
    }

    @Test
    void shouldGetExercisesDoneLastWeek() throws Exception {
        AssignedExerciseResponse assignedExerciseResponse = AssignedExerciseResponse.builder()
                .id(1L)
                .assignedBy(new DoctorResponse())
                .assignedTo(new PatientResponse())
                .exercise(new ExerciseResponse())
                .isDone(true)
                .dueDate(LocalDate.of(2024,10,10))
                .build();
        when(assignedExerciseService.getUserAssignedExercisesLastWeek(1L)).thenReturn(List.of(assignedExerciseResponse));
        mockMvc.perform(get("/exercises-done-last-week?id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].assignedBy").exists())
                .andExpect(jsonPath("$[0].assignedTo").exists())
                .andExpect(jsonPath("$[0].exercise").exists())
                .andExpect(jsonPath("$[0].done").exists())
                .andExpect(jsonPath("$[0].dueDate").exists());
        verify(assignedExerciseService, times(1)).getUserAssignedExercisesLastWeek(any());
    }

    @Test
    void shouldGetPatientAssignedExercisesToday() throws Exception {
        AssignedExerciseResponse assignedExerciseResponse = AssignedExerciseResponse.builder()
                .id(1L)
                .assignedBy(new DoctorResponse())
                .assignedTo(new PatientResponse())
                .exercise(new ExerciseResponse())
                .isDone(true)
                .dueDate(LocalDate.of(2024,10,10))
                .build();
        when(assignedExerciseService.getUserAssignedExercisesLastWeek(1L)).thenReturn(List.of(assignedExerciseResponse));
        mockMvc.perform(get("/exercises-done-last-week?id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].assignedBy").exists())
                .andExpect(jsonPath("$[0].assignedTo").exists())
                .andExpect(jsonPath("$[0].exercise").exists())
                .andExpect(jsonPath("$[0].done").exists())
                .andExpect(jsonPath("$[0].dueDate").exists());
        verify(assignedExerciseService, times(1)).getUserAssignedExercisesLastWeek(any());
    }

    @Test
    void shouldRemoveAssignedExercise() throws Exception {
        when(assignedExerciseService.removeAssignedExercise(1L)).thenReturn(true);
        mockMvc.perform(delete("/remove-assigned-exercise?id=1"))
                .andExpect(status().isOk());
        verify(assignedExerciseService, times(1)).removeAssignedExercise(any());
    }

    @Test
    void shouldNotRemoveAssignedExercise() throws Exception {
        when(assignedExerciseService.removeAssignedExercise(1L)).thenReturn(false);
        mockMvc.perform(delete("/remove-assigned-exercise?id=1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldNotSetExerciseStatus() throws Exception {
        when(assignedExerciseService.setExerciseStatus(1L)).thenReturn(false);
        mockMvc.perform(put("/set-exercise-status?id=1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldSetExerciseStatus() throws Exception {
        when(assignedExerciseService.setExerciseStatus(1L)).thenReturn(true);
        mockMvc.perform(put("/set-exercise-status?id=1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetAssignedExercisesToday() throws Exception {
        AssignedExerciseResponse assignedExerciseResponse = new AssignedExerciseResponse();
        when(assignedExerciseService.getUserAssignedExercisesToday(1L)).thenReturn(List.of(assignedExerciseResponse));
        mockMvc.perform(get("/patient-assigned-exercises-today?id=1"))
                .andExpect(status().isOk());
    }

}