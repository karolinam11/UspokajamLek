package com.example.uspokajamlekbackend.user.patient.dto;

import com.example.uspokajamlekbackend.user.patient.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditAccountRequest {
    private String email;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private Role role;
}
