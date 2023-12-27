package com.example.uspokajamlekbackend.user;

import com.example.uspokajamlekbackend.user.doctor.Doctor;
import com.example.uspokajamlekbackend.user.doctor.DoctorService;
import com.example.uspokajamlekbackend.user.patient.Patient;
import com.example.uspokajamlekbackend.user.patient.PatientService;
import com.example.uspokajamlekbackend.user.patient.Role;
import com.example.uspokajamlekbackend.user.patient.dto.PatientResponse;
import com.example.uspokajamlekbackend.user.patient.dto.SignupRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@AutoConfigureMockMvc
@SpringBootTest
public class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DoctorService doctorService;

    @Test
    public void shouldSignup() throws Exception {
        SignupRequest signupRequest = SignupRequest.builder()
                .email("email")
                .password("password")
                .address("address")
                .doctors(List.of())
                .birthDate(LocalDate.now())
                .phoneNumber("213312132")
                .role("patient")
                .name("name")
                .surname("surname")
                .build();
        Doctor doctor = Doctor.builder()
                .id(1L)
                .role(Role.PATIENT)
                .birthDate(LocalDate.now())
                .patients(List.of())
                .email("email")
                .password("password")
                .build();
        when(doctorService.signup(any())).thenReturn(doctor);
        mockMvc.perform(post("/doctor/signup-doctor")
                        .content(objectMapper.writeValueAsString(signupRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(doctorService, times(1)).signup(any());
    }

    @Test
    public void shouldNotSignup() throws Exception {
        SignupRequest signupRequest = SignupRequest.builder()
                .email("email")
                .password("password")
                .address("address")
                .doctors(List.of())
                .birthDate(LocalDate.now())
                .phoneNumber("213312132")
                .role("patient")
                .name("name")
                .surname("surname")
                .build();
        when(doctorService.signup(any())).thenThrow(EntityExistsException.class);
        mockMvc.perform(post("/doctor/signup-doctor")
                        .content(objectMapper.writeValueAsString(signupRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(doctorService, times(1)).signup(any());
    }

    @Test
    public void shouldGetDoctorById() throws Exception {
        when(doctorService.getDoctorById(1L)).thenReturn(new Doctor());
        mockMvc.perform(get("/doctor/1"))
                .andExpect(status().isOk());
        verify(doctorService, times(1)).getDoctorById(1L);
    }

    @Test
    public void shouldNotGetDoctorById() throws Exception {
        when(doctorService.getDoctorById(1L)).thenThrow(EntityNotFoundException.class);
        mockMvc.perform(get("/doctor/1"))
                .andExpect(status().isNotFound());
        verify(doctorService, times(1)).getDoctorById(1L);
    }

    @Test
    public void shouldGetPendingRequests() throws Exception {
        when(doctorService.getPendingRequests(1L)).thenReturn(List.of(new PatientResponse()));
        mockMvc.perform(get("/doctor/pending-requests?id=1"))
                .andExpect(status().isOk());
        verify(doctorService, times(1)).getPendingRequests(1L);
    }

    @Test
    public void shouldGetPatients() throws Exception {
        when(doctorService.getPatients(1L)).thenReturn(List.of(new PatientResponse()));
        mockMvc.perform(get("/doctor/patients?id=1"))
                .andExpect(status().isOk());
        verify(doctorService, times(1)).getPatients(1L);
    }
}
