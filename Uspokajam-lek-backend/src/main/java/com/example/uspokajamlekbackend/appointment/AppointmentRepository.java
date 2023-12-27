package com.example.uspokajamlekbackend.appointment;

import com.example.uspokajamlekbackend.assignedExercise.AssignedExercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> getAppointmentByDoctorId(Long doctorId);
    List<Appointment> getAppointmentByPatientId(Long patientId);

    Appointment getById(Long id);

}
