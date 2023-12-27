package com.example.uspokajamlekbackend.user;

import com.example.uspokajamlekbackend.security.JwtTokenUtil;
import com.example.uspokajamlekbackend.user.doctor.Doctor;
import com.example.uspokajamlekbackend.user.doctor.DoctorRepository;
import com.example.uspokajamlekbackend.user.dto.UserResponse;
import com.example.uspokajamlekbackend.user.patient.Patient;
import com.example.uspokajamlekbackend.user.patient.PatientRepository;
import com.example.uspokajamlekbackend.user.patient.Role;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.print.Doc;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private UserService userService;

    @Test
    void testLoginPatient() {
        String email = "patient@example.com";
        String password = "password";

        Patient patient = new Patient();
        patient.setId(1L);

        when(patientRepository.findByEmailAndPassword(email, password))
                .thenReturn(Optional.of(patient));
        when(jwtTokenUtil.generateToken(any(), any())).thenReturn("token");

        UserResponse userResponse = userService.login(email, password);

        assertEquals( ((Patient) userResponse.getUser()).getEmail(), patient.getEmail());

    }


    @Test
    void testLoginDoctor() {
        String email = "doctor@example.com";
        String password = "password";
        Doctor doctor = new Doctor();
        doctor.setId(1L);

        when(doctorRepository.findByEmailAndPassword(email, password))
                .thenReturn(Optional.of(doctor));
        when(jwtTokenUtil.generateToken(any(), any())).thenReturn("token");


        UserResponse userResponse = userService.login(email, password);

        assertEquals( ((Doctor) userResponse.getUser()).getEmail(), doctor.getEmail());
    }

    @Test
    void shouldThrowEntityNotFoundWhenUserDoesntExist() {
        String email = "unknown@example.com";
        String password = "password";

        when(patientRepository.findByEmailAndPassword(email, password))
                .thenReturn(Optional.empty());
        when(doctorRepository.findByEmailAndPassword(email, password))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.login(email, password));
    }
}
