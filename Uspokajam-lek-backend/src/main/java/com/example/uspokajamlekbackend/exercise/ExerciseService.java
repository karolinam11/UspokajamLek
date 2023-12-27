package com.example.uspokajamlekbackend.exercise;

import com.example.uspokajamlekbackend.user.doctor.DoctorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class ExerciseService {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DoctorService doctorService;

    public void addExercise(ExerciseRequest exerciseRequest) {
        Exercise exercise = modelMapper.map(exerciseRequest, Exercise.class);
        exercise.setCreatedBy(doctorService.getById(exerciseRequest.getCreatedBy()));
        exerciseRepository.save(exercise);
    }

    public void updateExercise(ExerciseRequest exerciseRequest) {
        Exercise exerciseDb = exerciseRepository.findById(exerciseRequest.getId()).orElseThrow(EntityNotFoundException::new);
        Exercise exerciseToBeUpdated = modelMapper.map(exerciseRequest, Exercise.class);
        exerciseToBeUpdated.setId(exerciseDb.getId());
        exerciseToBeUpdated.setCreatedBy(doctorService.getById(exerciseRequest.getCreatedBy()));
        exerciseRepository.save(exerciseToBeUpdated);
}

    public void deleteExercise(String name) {
        exerciseRepository.delete(exerciseRepository.getByName(name));
    }

    public List<ExerciseResponse> getAllExercises() {
        return exerciseRepository.findAll().stream().map(
                exercise -> modelMapper.map(exercise, ExerciseResponse.class)
        ).toList();
    }


}
