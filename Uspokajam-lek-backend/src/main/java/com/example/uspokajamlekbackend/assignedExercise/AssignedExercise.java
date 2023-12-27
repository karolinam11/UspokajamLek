package com.example.uspokajamlekbackend.assignedExercise;

import com.example.uspokajamlekbackend.user.doctor.Doctor;
import com.example.uspokajamlekbackend.exercise.Exercise;
import com.example.uspokajamlekbackend.user.patient.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity //encja, czyli bedzie zapis do bazy danych
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data //tworzy nam gettery i settery
public class AssignedExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Exercise exercise;
    private LocalDate dueDate;
    @ManyToOne
    private Patient assignedTo;
    @ManyToOne
    private Doctor assignedBy;

    @Enumerated(EnumType.STRING)
    private Status status;
    private boolean isDone = false;
}
