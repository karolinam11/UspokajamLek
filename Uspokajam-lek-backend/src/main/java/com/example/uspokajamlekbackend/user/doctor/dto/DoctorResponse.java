package com.example.uspokajamlekbackend.user.doctor.dto;

import com.example.uspokajamlekbackend.user.doctor.Doctor;
import com.example.uspokajamlekbackend.user.patient.dto.PatientResponse;
import com.example.uspokajamlekbackend.user.patient.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DoctorResponse {
    private Long id;
    private String email;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String specialization;
    private String address;
    private String phoneNumber;
    private Role role;
    private String invitationCode;
    private List<PatientResponse> patients;

}
