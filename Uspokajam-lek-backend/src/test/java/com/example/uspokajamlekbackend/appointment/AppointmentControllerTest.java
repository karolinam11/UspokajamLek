package com.example.uspokajamlekbackend.appointment;
import com.example.uspokajamlekbackend.user.doctor.dto.DoctorResponse;
import com.example.uspokajamlekbackend.user.patient.dto.PatientResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
@AutoConfigureMockMvc
@SpringBootTest
public class AppointmentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AppointmentService appointmentService;

    @Test
    void shouldGetDoctorsAppointments() throws Exception {
        AppointmentResponse appointmentResponse = AppointmentResponse.builder()
                .id(1L)
                .visitStartDate(LocalDateTime.of(2020,10,10,10,10,10))
                .visitEndDate(LocalDateTime.of(2020,10,10,11,10,10))
                .doctor(new DoctorResponse())
                .patient(new PatientResponse())
                .build();
        when(appointmentService.getFutureDoctorAppointment(1L)).thenReturn(List.of(appointmentResponse));

        mockMvc.perform(get("/doctor-appointments?id=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].doctor").exists())
                .andExpect(jsonPath("$[0].patient").exists())
                .andExpect(jsonPath("$[0].visitStartDate").exists())
                .andExpect(jsonPath("$[0].visitEndDate").exists());

        verify(appointmentService, times(1)).getFutureDoctorAppointment(1L);
    }

    @Test
    void shouldGetPatientsAppointments() throws Exception {
        AppointmentResponse appointmentResponse = AppointmentResponse.builder()
                .id(1L)
                .visitStartDate(LocalDateTime.of(2020,10,10,10,10,10))
                .visitEndDate(LocalDateTime.of(2020,10,10,11,10,10))
                .doctor(new DoctorResponse())
                .patient(new PatientResponse())
                .build();
        when(appointmentService.getFuturePatientAppointment(1L)).thenReturn(List.of(appointmentResponse));

        mockMvc.perform(get("/patient-appointments?id=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(appointmentService, times(1)).getFuturePatientAppointment(1L);
    }

    @Test
    void shouldThrowBadRequestWhenAddAppointment() throws Exception {
        AddAppointmentRequest addAppointmentRequest = AddAppointmentRequest.builder()
                .doctorId(1L)
                .patientId(1L)
                .visitStartDate(LocalDateTime.of(2020,10,10,10,10,10))
                .visitEndDate(LocalDateTime.of(2020,10,10,10,50,10))
                .build();
        AppointmentResponse appointmentResponse = AppointmentResponse.builder()
                .id(1L)
                .visitStartDate(LocalDateTime.of(2020,10,10,10,10,10))
                .visitEndDate(LocalDateTime.of(2020,10,10,11,10,10))
                .doctor(new DoctorResponse())
                .patient(new PatientResponse())
                .build();
        when(appointmentService.addAppointment(addAppointmentRequest)).thenReturn(appointmentResponse);

        mockMvc.perform(post("/add-appointment")
                        .content(objectMapper.writeValueAsString(addAppointmentRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldAddAppointment() throws Exception {
        AddAppointmentRequest addAppointmentRequest = AddAppointmentRequest.builder()
                .doctorId(1L)
                .patientId(1L)
                .visitStartDate(LocalDateTime.of(2024,10,10,10,10,10))
                .visitEndDate(LocalDateTime.of(2024,10,10,10,50,10))
                .build();
        AppointmentResponse appointmentResponse = AppointmentResponse.builder()
                .id(1L)
                .visitStartDate(LocalDateTime.of(2020,10,10,10,10,10))
                .visitEndDate(LocalDateTime.of(2020,10,10,11,10,10))
                .doctor(new DoctorResponse())
                .patient(new PatientResponse())
                .build();
        when(appointmentService.addAppointment(addAppointmentRequest)).thenReturn(appointmentResponse);

        mockMvc.perform(post("/add-appointment")
                        .content(objectMapper.writeValueAsString(addAppointmentRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(appointmentService, times(1)).addAppointment(any());
    }

    @Test
    void shouldGetDoctorsPastAppointments() throws Exception {
        AppointmentResponse appointmentResponse = AppointmentResponse.builder()
                .id(1L)
                .visitStartDate(LocalDateTime.of(2020,10,10,10,10,10))
                .visitEndDate(LocalDateTime.of(2020,10,10,11,10,10))
                .doctor(new DoctorResponse())
                .patient(new PatientResponse())
                .build();
        when(appointmentService.getPastDoctorAppointments(1L)).thenReturn(List.of(appointmentResponse));

        mockMvc.perform(get("/doctor-past-appointments?id=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(appointmentService, times(1)).getPastDoctorAppointments(1L);
    }

    @Test
    void shouldGetPatientsPastAppointments() throws Exception {
        AppointmentResponse appointmentResponse = AppointmentResponse.builder()
                .id(1L)
                .visitStartDate(LocalDateTime.of(2020,10,10,10,10,10))
                .visitEndDate(LocalDateTime.of(2020,10,10,11,10,10))
                .doctor(new DoctorResponse())
                .patient(new PatientResponse())
                .build();
        when(appointmentService.getFuturePatientAppointment(1L)).thenReturn(List.of(appointmentResponse));

        mockMvc.perform(get("/patient-past-appointments?id=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(appointmentService, times(1)).getPastPatientAppointments(1L);
    }

    @Test
    void shouldDeleteAppointment() throws Exception {
        when(appointmentService.removeAppointment(1L)).thenReturn(true);

        mockMvc.perform(delete("/remove-appointment?id=1"))
                .andExpect(status().isOk());
        verify(appointmentService, times(1)).removeAppointment(1L);
    }

    @Test
    void shouldNotDeleteAppointment() throws Exception {
        when(appointmentService.removeAppointment(1L)).thenReturn(false);

        mockMvc.perform(delete("/remove-appointment?id=1"))
                .andExpect(status().isNotFound());
        verify(appointmentService, times(1)).removeAppointment(1L);
    }
}
