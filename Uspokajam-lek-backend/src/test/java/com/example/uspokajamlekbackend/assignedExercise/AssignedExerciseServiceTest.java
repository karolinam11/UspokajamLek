package com.example.uspokajamlekbackend.assignedExercise;

import com.example.uspokajamlekbackend.assignedExercise.dto.AssignExerciseRequest;
import com.example.uspokajamlekbackend.assignedExercise.dto.AssignedExerciseResponse;
import com.example.uspokajamlekbackend.exercise.Exercise;
import com.example.uspokajamlekbackend.exercise.ExerciseRepository;
import com.example.uspokajamlekbackend.user.doctor.Doctor;
import com.example.uspokajamlekbackend.user.doctor.DoctorRepository;
import com.example.uspokajamlekbackend.user.patient.Patient;
import com.example.uspokajamlekbackend.user.patient.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AssignedExerciseServiceTest {

    @Mock
    private AssignedExerciseRepository assignedExerciseRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AssignedExerciseService assignedExerciseService;

    @Test
    void shouldAssignExercise() {
        AssignExerciseRequest assignExerciseRequest = new AssignExerciseRequest(1L,2L, LocalDate.now(), 2L);

        Exercise exercise = new Exercise();
        Doctor doctor = new Doctor();
        Patient patient = new Patient();

        when(exerciseRepository.getById(assignExerciseRequest.getExerciseId())).thenReturn(exercise);
        when(doctorRepository.getById(assignExerciseRequest.getDoctorId())).thenReturn(doctor);
        when(patientRepository.getById(assignExerciseRequest.getPatientId())).thenReturn(patient);

        assignedExerciseService.assignExercise(assignExerciseRequest);

        verify(assignedExerciseRepository, times(1)).save(any(AssignedExercise.class));
    }

    @Test
    void shouldGetUserAssignedExercises() {
        Long patientId = 1L;

        List<AssignedExercise> assignedExercises = Arrays.asList(
                new AssignedExercise(), new AssignedExercise(), new AssignedExercise()
        );

        when(assignedExerciseRepository.getAssignedExerciseByAssignedToId(patientId)).thenReturn(assignedExercises);
        when(modelMapper.map(any(AssignedExercise.class), eq(AssignedExerciseResponse.class)))
                .thenAnswer(invocation -> {
                    AssignedExercise exercise = invocation.getArgument(0);
                    return new AssignedExerciseResponse();
                });

        List<AssignedExerciseResponse> userAssignedExercises = assignedExerciseService.getUserAssignedExercises(patientId);

        assertEquals(3, userAssignedExercises.size());
    }


    @Test
    void shouldRemoveAssignedExercise() {
        Long exerciseId = 1L;

        AssignedExercise assignedExercise = new AssignedExercise();
        when(assignedExerciseRepository.findById(exerciseId)).thenReturn(Optional.of(assignedExercise));

        assertTrue(assignedExerciseService.removeAssignedExercise(exerciseId));

        verify(assignedExerciseRepository, times(1)).delete(assignedExercise);
    }

    @Test
    void shouldSetExerciseStatus() {
        Long exerciseId = 1L;

        AssignedExercise assignedExercise = new AssignedExercise();
        when(assignedExerciseRepository.getById(exerciseId)).thenReturn(assignedExercise);

        assertTrue(assignedExerciseService.setExerciseStatus(exerciseId));

        assertTrue(assignedExercise.isDone());
        verify(assignedExerciseRepository, times(1)).save(assignedExercise);
    }

    @Test
    void shouldSetExerciseAsSeen() {
        AssignedExercise exercise = new AssignedExercise();

        when(assignedExerciseRepository.save(exercise)).thenReturn(exercise);

        AssignedExercise seenExercise = assignedExerciseService.setExerciseAsSeen(exercise);

        assertEquals(Status.SEEN, seenExercise.getStatus());
        verify(assignedExerciseRepository, times(1)).save(exercise);
    }
}

