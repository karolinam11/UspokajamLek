package com.example.uspokajamlekbackend.appointment;

import com.example.uspokajamlekbackend.user.doctor.dto.DoctorResponse;
import com.example.uspokajamlekbackend.user.patient.dto.PatientResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AppointmentResponse {
    private Long id;
    private LocalDateTime visitStartDate;
    private LocalDateTime visitEndDate;
    private DoctorResponse doctor;
    private PatientResponse patient;
}
