package com.example.uspokajamlekbackend.user.doctor.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class AddPatientRequest {
    private long doctorId;
    private long patientId;
}
