package com.example.uspokajamlekbackend.user.patient;

import com.example.uspokajamlekbackend.appointment.Appointment;
import com.example.uspokajamlekbackend.user.User;
import com.example.uspokajamlekbackend.user.doctor.Doctor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Table(name="users")
@Entity //encja, czyli bedzie zapis do bazy danych
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data //tworzy nam gettery i settery
public class Patient implements User {

    @Id
    @Column(name="user_id")
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "patient_doctor",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "doctor_id")
    )
    @ToString.Exclude
    @JsonIgnore
    private List<Doctor> doctors = new ArrayList<>();
}
