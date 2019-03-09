package com.example.waste_management_server.repository;

import com.example.waste_management_server.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface VehicleRepo extends JpaRepository<Vehicle, Integer> {

    Collection<Vehicle> findByDate(String date);
}
