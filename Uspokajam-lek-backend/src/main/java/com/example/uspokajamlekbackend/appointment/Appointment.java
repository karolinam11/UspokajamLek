package com.example.uspokajamlekbackend.appointment;

import com.example.uspokajamlekbackend.user.doctor.Doctor;
import com.example.uspokajamlekbackend.user.patient.Patient;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data //tworzy nam gettery i settery
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO  )
    private Long id;
    @NonNull
    private LocalDateTime visitStartDate;

    @NonNull
    private LocalDateTime visitEndDate;
    @NonNull
    @ManyToOne
    @ToString.Exclude
    private Patient patient;
    @NonNull
    @ManyToOne
    @ToString.Exclude
    private Doctor doctor;

}
