package com.example.waste_management_server.repository;

import com.example.waste_management_server.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface VehicleRepo extends JpaRepository<Vehicle, Integer> {

    List<Vehicle> findByDate(String date);
}
