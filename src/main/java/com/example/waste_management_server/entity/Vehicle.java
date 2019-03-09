package com.example.waste_management_server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "vehicle")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "vehicle_id")
    private Integer vehicleId;

    @Column(name="path_followed")
    private String pathFollowed;

    @Column(name = "total_distance_travelled")
    private double totalDistanceTravelled;

    @Column(name = "date")
    private String date;
}

