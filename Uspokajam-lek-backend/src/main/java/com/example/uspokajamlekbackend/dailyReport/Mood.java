package com.example.uspokajamlekbackend.dailyReport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class Mood {
    private String name;
    private LocalDate date;
}
