package com.example.uspokajamlekbackend.assignedExercise.dto;

import com.example.uspokajamlekbackend.assignedExercise.Status;
import com.example.uspokajamlekbackend.user.doctor.dto.DoctorResponse;
import com.example.uspokajamlekbackend.exercise.ExerciseResponse;
import com.example.uspokajamlekbackend.user.patient.dto.PatientResponse;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Setter
@AllArgsConstructor
@Builder
public class AssignedExerciseResponse {
    private Long id;
    private ExerciseResponse exercise;
    private LocalDate dueDate;
    private PatientResponse assignedTo;
    private DoctorResponse assignedBy;
    private Status status;
    private boolean isDone;
}
