package com.example.uspokajamlekbackend.activity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ActivityResponse {

    private String name;
    private LocalDate date;
    private String mood;
}
