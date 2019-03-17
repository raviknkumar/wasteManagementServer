package com.example.waste_management_server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsInfoDto {

    private Integer numberOfDustbins;
    private Double totalDistanceTravlled;
    private Double totalFillAmount;
    private List<Integer> dustbinIds;
    private List<Double> fillAmounts;

}
