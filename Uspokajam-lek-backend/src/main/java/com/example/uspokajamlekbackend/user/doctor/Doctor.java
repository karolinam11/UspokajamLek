package com.example.uspokajamlekbackend.user.doctor;

import com.example.uspokajamlekbackend.appointment.Appointment;
import com.example.uspokajamlekbackend.assignedExercise.AssignedExercise;
import com.example.uspokajamlekbackend.user.User;
import com.example.uspokajamlekbackend.user.patient.Role;
import com.example.uspokajamlekbackend.user.patient.Patient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "doctors")
@ToString
public class Doctor implements User {
    @Id
    @Column(name = "doctor_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String email;
    @NonNull
    private String password;
    private String name;
    private String surname;
    private LocalDate birthDate;
    @Enumerated(EnumType.STRING)
    private Role role;

    private String specialization;
    private String address;

    private String phoneNumber;

    private String invitationCode;

    @ManyToMany(mappedBy = "doctors", fetch = FetchType.EAGER)
    @ToString.Exclude
    @JsonIgnore
    private List<Patient> patients = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "pending_request",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @ToString.Exclude
    @JsonIgnore
    private List<Patient> pendingRequests = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ToString.Exclude
    @JsonIgnore
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "assignedBy", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private List<AssignedExercise> exercisesAssignedByUser;

}
