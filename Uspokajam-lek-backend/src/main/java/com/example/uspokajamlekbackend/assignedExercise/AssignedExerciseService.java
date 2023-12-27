package com.example.uspokajamlekbackend.assignedExercise;

import com.example.uspokajamlekbackend.assignedExercise.dto.AssignExerciseRequest;
import com.example.uspokajamlekbackend.assignedExercise.dto.AssignedExerciseResponse;
import com.example.uspokajamlekbackend.user.doctor.DoctorRepository;
import com.example.uspokajamlekbackend.user.doctor.dto.DoctorResponse;
import com.example.uspokajamlekbackend.exercise.ExerciseRepository;
import com.example.uspokajamlekbackend.exercise.ExerciseResponse;
import com.example.uspokajamlekbackend.user.patient.PatientRepository;
import com.example.uspokajamlekbackend.user.patient.dto.PatientResponse;
import jakarta.persistence.EntityExistsException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AssignedExerciseService {

    @Autowired
    private AssignedExerciseRepository assignedExerciseRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ModelMapper modelMapper;


    public void assignExercise(AssignExerciseRequest assignExerciseRequest) {
        AssignedExercise assignedExercise = AssignedExercise.builder()
                .exercise(exerciseRepository.getById(assignExerciseRequest.getExerciseId()))
                .assignedBy(doctorRepository.getById(assignExerciseRequest.getDoctorId()))
                .assignedTo(patientRepository.getById(assignExerciseRequest.getPatientId()))
                .status(Status.NEW)
                .dueDate(assignExerciseRequest.getDueDate())
                .isDone(false)
                .build();
        this.assignedExerciseRepository.save(assignedExercise);
    }

    public List<AssignedExerciseResponse> getUserAssignedExercises(Long patientId) {
        return this.assignedExerciseRepository.getAssignedExerciseByAssignedToId(patientId)
                .stream().map(exercise ->
                        modelMapper.map(exercise, AssignedExerciseResponse.class)).toList();
    }

    public List<AssignedExerciseResponse> getNewUserAssignedExercises(Long patientId){
        List<AssignedExercise> newExercises = this.assignedExerciseRepository.getAssignedExerciseByAssignedToIdAndStatus(patientId, Status.NEW);

        newExercises = newExercises.stream().map(exercise -> setExerciseAsSeen(exercise)).toList();
        return newExercises.stream().map(exercise -> {
            return modelMapper.map(exercise, AssignedExerciseResponse.class);
                }).toList();
    }

    public List<AssignedExerciseResponse> getUserAssignedExercisesToday(Long patientId){
        return this.assignedExerciseRepository.getAssignedExerciseByAssignedToId(patientId)
                .stream()
                .filter(exercise -> {
                    return exercise.getDueDate().equals(LocalDate.now()) && !exercise.isDone();
                })
                .map(exercise -> modelMapper.map(exercise, AssignedExerciseResponse.class)
                ).toList();
    }

    public List<AssignedExerciseResponse> getUserAssignedExercisesLastWeek(Long patientId){
        return this.assignedExerciseRepository.getAssignedExerciseByAssignedToIdAndDueDateIsGreaterThan(patientId, LocalDate.now().minusDays(7))
                .stream()
                .filter(exercise -> {
                    return exercise.isDone();
                })
                .map(exercise -> modelMapper.map(exercise, AssignedExerciseResponse.class)
                ).toList();
    }

    public boolean removeAssignedExercise(Long exerciseId) {
        AssignedExercise exercise = assignedExerciseRepository.findById(exerciseId).orElseThrow(EntityExistsException::new);
        if (exercise != null) {
            assignedExerciseRepository.delete(exercise);
            return true;
        }
        return false;
    }

    public boolean setExerciseStatus(Long exerciseId) {
        AssignedExercise exercise = assignedExerciseRepository.getById(exerciseId);
        if (exercise != null) {
            exercise.setDone(true);
            assignedExerciseRepository.save(exercise);
            return true;
        }
        return false;
    }

    public AssignedExercise setExerciseAsSeen(AssignedExercise exercise) {
        exercise.setStatus(Status.SEEN);
        return assignedExerciseRepository.save(exercise);
    }


}
