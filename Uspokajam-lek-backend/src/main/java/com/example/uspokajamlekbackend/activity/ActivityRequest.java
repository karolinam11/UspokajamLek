package com.example.uspokajamlekbackend.activity;

import lombok.Data;

import java.time.LocalDate;

@Data

public class ActivityRequest {
    private String name;
    private String mood;
    private LocalDate date;
    private Long userId;

}
