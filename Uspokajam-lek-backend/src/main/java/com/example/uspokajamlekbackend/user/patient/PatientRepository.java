package com.example.uspokajamlekbackend.user.patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    boolean existsByEmail(String email);

    Optional<Patient> findByEmailAndPassword(String email, String password);

    Optional<Patient> findByEmail(String email);

}
