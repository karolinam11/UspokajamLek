package com.example.uspokajamlekbackend.user.doctor.dto;

import com.example.uspokajamlekbackend.user.patient.dto.EditAccountRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EditDoctorAccountRequest extends EditAccountRequest {
    private String specialization;
    private String address;
    private String phoneNumber;
}
