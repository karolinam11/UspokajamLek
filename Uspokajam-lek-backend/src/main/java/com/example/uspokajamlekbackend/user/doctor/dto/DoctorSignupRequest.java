package com.example.uspokajamlekbackend.user.doctor.dto;

import com.example.uspokajamlekbackend.user.patient.Role;
import com.example.uspokajamlekbackend.user.patient.dto.SignupRequest;

public class DoctorSignupRequest extends SignupRequest {
    private String specialization;
    private String address;
    private String phoneNumber;
    private String role;
}
