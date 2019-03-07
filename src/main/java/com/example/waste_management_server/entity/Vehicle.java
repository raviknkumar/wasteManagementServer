package com.example.waste_management_server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @ElementCollection
    @CollectionTable(name="dustbin", joinColumns= {@JoinColumn(name="dustbin_id")})
    @Column(name="path_followed", nullable=false)
    private List<Integer> pathFollowed;

    @Column(name = "total_distance_travelled")
    private double totalDistanceTravelled;

}
