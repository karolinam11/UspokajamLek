package com.example.uspokajamlekbackend.dailyReport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class MoodsRequest {
    private int numOfDays;
    private Long userId;
}
