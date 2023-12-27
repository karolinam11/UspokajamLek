package com.example.uspokajamlekbackend.appointment;

import com.example.uspokajamlekbackend.user.doctor.Doctor;
import com.example.uspokajamlekbackend.user.doctor.DoctorService;
import com.example.uspokajamlekbackend.user.patient.Patient;
import com.example.uspokajamlekbackend.user.patient.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientService patientService;

    @Mock
    private DoctorService doctorService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    void testAddAppointment() {
        AddAppointmentRequest addAppointmentRequest = new AddAppointmentRequest();

        Appointment appointment = new Appointment();
        appointment.setVisitStartDate(LocalDateTime.of(2000, 10, 10, 10, 10));
        AppointmentResponse appointmentResponse = new AppointmentResponse();
        appointmentResponse.setVisitStartDate(LocalDateTime.of(2000, 10, 10, 10, 10));
        when(modelMapper.map(addAppointmentRequest, Appointment.class)).thenReturn(appointment);
        when(doctorService.getById(addAppointmentRequest.getDoctorId())).thenReturn(new Doctor());
        when(patientService.getById(addAppointmentRequest.getPatientId())).thenReturn(new Patient());
        when(appointmentRepository.save(appointment)).thenReturn(appointment);
        when(modelMapper.map(appointment, AppointmentResponse.class)).thenReturn(appointmentResponse);

        AppointmentResponse addedAppointment = appointmentService.addAppointment(addAppointmentRequest);

        assertEquals(appointmentResponse, addedAppointment);
    }

    @Test
    void testGetFutureDoctorAppointment() {
        Long doctorId = 1L;
        Appointment appointment = new Appointment();
        appointment.setVisitStartDate(LocalDateTime.now().plusDays(1));
        List<Appointment> appointments = Arrays.asList(
                appointment, appointment, appointment
        );

        when(appointmentRepository.getAppointmentByDoctorId(doctorId)).thenReturn(appointments);
        when(modelMapper.map(any(Appointment.class), eq(AppointmentResponse.class)))
                .thenReturn(new AppointmentResponse());

        List<AppointmentResponse> futureDoctorAppointments = appointmentService.getFutureDoctorAppointment(doctorId);

        assertEquals(3, futureDoctorAppointments.size());
    }

    @Test
    void testGetFuturePatientAppointment() {
        Long patientId = 1L;
        Appointment appointment = new Appointment();
        appointment.setVisitStartDate(LocalDateTime.now().plusDays(1));
        List<Appointment> appointments = Arrays.asList(
                appointment, appointment, appointment
        );

        when(appointmentRepository.getAppointmentByPatientId(patientId)).thenReturn(appointments);
        when(modelMapper.map(any(Appointment.class), eq(AppointmentResponse.class)))
                .thenReturn(new AppointmentResponse());

        List<AppointmentResponse> futurePatientAppointments = appointmentService.getFuturePatientAppointment(patientId);

        assertEquals(3, futurePatientAppointments.size());
    }

    @Test
    void testGetPastPatientAppointments() {
        Long patientId = 1L;
        Appointment appointment = new Appointment();
        appointment.setVisitStartDate(LocalDateTime.now().minusDays(1));
        List<Appointment> appointments = Arrays.asList(
                appointment, appointment, appointment
        );

        when(appointmentRepository.getAppointmentByPatientId(patientId)).thenReturn(appointments);
        when(modelMapper.map(any(Appointment.class), eq(AppointmentResponse.class)))
                .thenReturn(new AppointmentResponse());

        List<AppointmentResponse> pastPatientAppointments = appointmentService.getPastPatientAppointments(patientId);

        assertEquals(3, pastPatientAppointments.size());
    }

    @Test
    void testGetPastDoctorAppointments() {
        Long doctorId = 1L;
        Appointment appointment = new Appointment();
        appointment.setVisitStartDate(LocalDateTime.now().minusDays(1));
        List<Appointment> appointments = Arrays.asList(
                appointment, appointment, appointment
        );

        when(appointmentRepository.getAppointmentByDoctorId(doctorId)).thenReturn(appointments);
        when(modelMapper.map(any(Appointment.class), eq(AppointmentResponse.class)))
                .thenReturn(new AppointmentResponse());

        List<AppointmentResponse> pastDoctorAppointments = appointmentService.getPastDoctorAppointments(doctorId);

        assertEquals(3, pastDoctorAppointments.size());
    }

    @Test
    void testRemoveAppointmentWhenAppointmentNotFound() {
        Long appointmentId = 1L;

        when(appointmentRepository.getById(appointmentId)).thenReturn(null);

        assertFalse(appointmentService.removeAppointment(appointmentId));

        verify(appointmentRepository, never()).deleteById(appointmentId);
    }

}
