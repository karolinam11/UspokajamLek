package com.example.uspokajamlekbackend.exercise;

import com.example.uspokajamlekbackend.user.doctor.Doctor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ExerciseResponse {
    private Long id;
    private String name;
    private String description;
    private String category;
    private int time;
    private String duration;
    private Doctor createdBy;
}
