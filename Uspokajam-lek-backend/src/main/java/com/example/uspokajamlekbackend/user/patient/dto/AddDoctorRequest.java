package com.example.uspokajamlekbackend.user.patient.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AddDoctorRequest {
    private long patientId;
    private String invitationCode;

}
