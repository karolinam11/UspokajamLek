package com.example.uspokajamlekbackend.user;

import com.example.uspokajamlekbackend.user.doctor.Doctor;
import com.example.uspokajamlekbackend.user.doctor.DoctorRepository;
import com.example.uspokajamlekbackend.user.doctor.DoctorService;
import com.example.uspokajamlekbackend.user.doctor.dto.AddPatientRequest;
import com.example.uspokajamlekbackend.user.patient.Patient;
import com.example.uspokajamlekbackend.user.patient.PatientRepository;
import com.example.uspokajamlekbackend.user.patient.Role;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DoctorService doctorService;


    @Test
    void shouldSignupNewDoctor() {
        Doctor doctor = new Doctor();
        doctor.setEmail("newdoctor@example.com");
        doctor.setName("Dr. Smith");

        when(doctorRepository.existsByEmail(doctor.getEmail())).thenReturn(false);
        when(doctorRepository.save(doctor)).thenReturn(doctor);

        Doctor savedDoctor = doctorService.signup(doctor);

        assertEquals(doctor.getEmail(), savedDoctor.getEmail());
        assertEquals(doctor.getName(), savedDoctor.getName());
        assertEquals(Role.DOCTOR, savedDoctor.getRole());
        assertNotNull(savedDoctor.getInvitationCode());
    }

    @Test
    void shouldSignupExistingDoctor() {
        Doctor existingDoctor = new Doctor();
        existingDoctor.setEmail("existingdoctor@example.com");

        when(doctorRepository.existsByEmail(existingDoctor.getEmail())).thenReturn(true);

        assertThrows(EntityExistsException.class, () -> doctorService.signup(existingDoctor));
    }

    @Test
    void shouldEditDoctorAccount() {
        Doctor existingDoctor = new Doctor();
        existingDoctor.setEmail("existingdoctor@example.com");
        existingDoctor.setName("Dr. OldName");

        Doctor updatedDoctorDetails = new Doctor();
        updatedDoctorDetails.setEmail("existingdoctor@example.com");
        updatedDoctorDetails.setName("Dr. NewName");

        when(doctorRepository.findByEmail(updatedDoctorDetails.getEmail())).thenReturn(Optional.of(existingDoctor));
        when(doctorRepository.save(any(Doctor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Doctor editedDoctor = doctorService.editDoctorAccount(updatedDoctorDetails);

        assertEquals(updatedDoctorDetails.getEmail(), editedDoctor.getEmail());
        assertEquals(updatedDoctorDetails.getName(), editedDoctor.getName());
    }

    @Test
    void shouldAssignPatientToDoctor() {
        Long doctorId = 1L;
        Long patientId = 2L;

        Doctor doctor = new Doctor();
        doctor.setId(doctorId);

        Patient patient = new Patient();
        patient.setId(patientId);

        AddPatientRequest addPatientRequest = new AddPatientRequest();
        addPatientRequest.setDoctorId(doctorId);
        addPatientRequest.setPatientId(patientId);

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertTrue(doctor.getPatients().isEmpty());

        boolean assigned = doctorService.assignPatientToDoctor(addPatientRequest);

        assertTrue(assigned);
        assertTrue(patient.getDoctors().contains(doctor));
    }

    @Test
    void shouldAcceptDoctorRequest() {
        Long doctorId = 1L;
        Long patientId = 2L;

        Doctor doctor = new Doctor();
        doctor.setId(doctorId);

        Patient patient = new Patient();
        patient.setId(patientId);

        doctor.getPendingRequests().add(patient);

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        assertTrue(doctor.getPendingRequests().contains(patient));
        assertTrue(doctor.getPatients().isEmpty());

        doctorService.acceptDoctorRequest(patientId, doctorId);

        assertFalse(doctor.getPendingRequests().contains(patient));
        assertTrue(doctor.getPatients().contains(patient));
    }

    @Test
    void shouldDeclineDoctorRequest() {
        Long doctorId = 1L;
        Long patientId = 2L;

        Doctor doctor = new Doctor();
        doctor.setId(doctorId);

        Patient patient = new Patient();
        patient.setId(patientId);

        doctor.getPendingRequests().add(patient);

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        assertTrue(doctor.getPendingRequests().contains(patient));

        doctorService.declineDoctorRequest(patientId, doctorId);

        assertFalse(doctor.getPendingRequests().contains(patient));
    }

}
