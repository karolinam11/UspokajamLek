package com.example.uspokajamlekbackend.user;

import com.example.uspokajamlekbackend.user.doctor.Doctor;
import com.example.uspokajamlekbackend.user.doctor.dto.DoctorResponse;
import com.example.uspokajamlekbackend.user.doctor.dto.EditDoctorAccountRequest;
import com.example.uspokajamlekbackend.user.dto.UserResponse;
import com.example.uspokajamlekbackend.user.patient.Patient;
import com.example.uspokajamlekbackend.user.patient.PatientService;
import com.example.uspokajamlekbackend.user.patient.Role;
import com.example.uspokajamlekbackend.user.patient.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityExistsException;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@AutoConfigureMockMvc
@SpringBootTest
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PatientService patientService;

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
        Patient patient = Patient.builder()
                .id(1L)
                .role(Role.PATIENT)
                .birthDate(LocalDate.now())
                .doctors(List.of())
                .email("email")
                .password("password")
                .build();
        when(patientService.signup(any())).thenReturn(patient);
        mockMvc.perform(post("/signup")
                        .content(objectMapper.writeValueAsString(signupRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(patientService, times(1)).signup(any());
    }

    @Test
    public void shouldNotSignupWhenEmailAlreadyExists() throws Exception {
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
        when(patientService.signup(any())).thenThrow(EntityExistsException.class);
        mockMvc.perform(post("/signup")
                        .content(objectMapper.writeValueAsString(signupRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(patientService, times(1)).signup(any());
    }

    @Test
    public void shouldEditPatientAccount() throws Exception {
        EditAccountRequest editAccountRequest = EditAccountRequest.builder()
                .email("email")
                .birthDate(LocalDate.now())
                .role(Role.PATIENT)
                .name("name")
                .surname("surname")
                .build();
        Patient editedPatient = Patient.builder()
                .id(1L)
                .role(Role.PATIENT)
                .birthDate(LocalDate.now())
                .doctors(List.of())
                .email("email")
                .password("password")
                .build();
        when(patientService.editAccount(any())).thenReturn(editedPatient);
        mockMvc.perform(put("/edit-account")
                        .content(objectMapper.writeValueAsString(editAccountRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(patientService, times(1)).editAccount(any());
    }

    @Test
    public void shouldAddDoctorRequest() throws Exception {
        AddDoctorRequest addDoctorRequest = AddDoctorRequest.builder()
                .patientId(1L)
                .invitationCode("DSASDA")
                .build();
        when(patientService.createDoctorRequest(any())).thenReturn(true);
        mockMvc.perform(post("/add-doctor-request")
                        .content(objectMapper.writeValueAsString(addDoctorRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(patientService, times(1)).createDoctorRequest(any());
    }

    @Test
    public void shouldNotAddDoctorRequest() throws Exception {
        AddDoctorRequest addDoctorRequest = AddDoctorRequest.builder()
                .patientId(1L)
                .invitationCode("DSASDA")
                .build();
        when(patientService.createDoctorRequest(any())).thenReturn(false);
        mockMvc.perform(post("/add-doctor-request")
                        .content(objectMapper.writeValueAsString(addDoctorRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(patientService, times(1)).createDoctorRequest(any());
    }

    @Test
    public void shouldGetPatientDoctors() throws Exception {
        when(patientService.getMyDoctors(any())).thenReturn(List.of(new Doctor()));
        mockMvc.perform(get("/my-doctors/1"))
                .andExpect(status().isOk());
        verify(patientService, times(1)).getMyDoctors(1L);
    }
}
