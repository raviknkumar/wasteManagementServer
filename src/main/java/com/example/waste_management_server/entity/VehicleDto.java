package com.example.waste_management_server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDto {

    private Integer vehicleId;
    private List<Integer> pathFollowed;
    private double totalDistanceTravelled;
    private String date;
}
