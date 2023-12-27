package com.example.uspokajamlekbackend.user.doctor;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByInvitationCode(String invitationCode);

    boolean existsByEmail(String email);
    Optional<Doctor> findByEmailAndPassword(String email, String password);

    Optional<Doctor> findByEmail(String email);
}
