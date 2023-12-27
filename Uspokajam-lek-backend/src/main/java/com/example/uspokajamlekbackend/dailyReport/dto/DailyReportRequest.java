package com.example.uspokajamlekbackend.dailyReport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyReportRequest {
    private LocalDate date;
    private String mood;
    private String note;
}
