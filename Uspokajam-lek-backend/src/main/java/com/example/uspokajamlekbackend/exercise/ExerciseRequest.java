package com.example.uspokajamlekbackend.exercise;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseRequest {

    private Long id;
    private String name;
    private String description;
    private String category;
    private int time;
    private Long createdBy;
    private String duration;
}
