package com.example.uspokajamlekbackend.exercise;

import com.example.uspokajamlekbackend.user.doctor.Doctor;
import com.example.uspokajamlekbackend.user.doctor.DoctorService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExerciseServiceTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private ExerciseService exerciseService;

    @Test
    void shouldAddExercise() {
        ExerciseRequest exerciseRequest = new ExerciseRequest();
        exerciseRequest.setCreatedBy(1L);

        Doctor doctor = new Doctor();
        doctor.setId(1L);

        Exercise exercise = new Exercise();
        exercise.setId(1L);

        when(modelMapper.map(exerciseRequest, Exercise.class)).thenReturn(exercise);
        when(doctorService.getById(exerciseRequest.getCreatedBy())).thenReturn(doctor);

        exerciseService.addExercise(exerciseRequest);

        verify(exerciseRepository, times(1)).save(exercise);
    }

    @Test
    void shouldUpdateExercise() {
        ExerciseRequest exerciseRequest = new ExerciseRequest();
        exerciseRequest.setId(1L);
        exerciseRequest.setCreatedBy(1L);

        Exercise exerciseDb = new Exercise();
        exerciseDb.setId(1L);

        Exercise updatedExercise = new Exercise();
        updatedExercise.setId(1L);

        when(exerciseRepository.findById(exerciseRequest.getId())).thenReturn(Optional.of(exerciseDb));
        when(modelMapper.map(exerciseRequest, Exercise.class)).thenReturn(updatedExercise);
        when(doctorService.getById(exerciseRequest.getCreatedBy())).thenReturn(new Doctor());

        exerciseService.updateExercise(exerciseRequest);

        verify(exerciseRepository, times(1)).save(updatedExercise);
    }

    @Test
    void shouldDeleteExercise() {
        String exerciseName = "Exercise 1";

        Exercise exercise = new Exercise();
        exercise.setName(exerciseName);

        when(exerciseRepository.getByName(exerciseName)).thenReturn(exercise);

        assertDoesNotThrow(() -> exerciseService.deleteExercise(exerciseName));

        verify(exerciseRepository, times(1)).delete(exercise);
    }

    @Test
    void shouldGetAllExercises() {
        Exercise exercise1 = new Exercise();
        exercise1.setId(1L);
        Exercise exercise2 = new Exercise();
        exercise2.setId(2L);

        List<Exercise> exercises = Arrays.asList(exercise1, exercise2);

        ExerciseResponse exerciseResponse1 = new ExerciseResponse();
        exerciseResponse1.setId(1L);
        ExerciseResponse exerciseResponse2 = new ExerciseResponse();
        exerciseResponse2.setId(2L);

        when(exerciseRepository.findAll()).thenReturn(exercises);
        when(modelMapper.map(exercise1, ExerciseResponse.class)).thenReturn(exerciseResponse1);
        when(modelMapper.map(exercise2, ExerciseResponse.class)).thenReturn(exerciseResponse2);

        List<ExerciseResponse> allExercises = exerciseService.getAllExercises();

        assertEquals(2, allExercises.size());
        assertEquals(1L, allExercises.get(0).getId());
        assertEquals(2L, allExercises.get(1).getId());
    }

    @Test
    void shouldUpdateExercise_EntityNotFoundException() {
        ExerciseRequest exerciseRequest = new ExerciseRequest();
        exerciseRequest.setId(1L);
        exerciseRequest.setCreatedBy(1L);

        when(exerciseRepository.findById(exerciseRequest.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> exerciseService.updateExercise(exerciseRequest));
    }
}
