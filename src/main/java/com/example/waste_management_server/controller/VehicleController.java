package com.example.waste_management_server.controller;


import com.example.waste_management_server.entity.Vehicle;
import com.example.waste_management_server.repository.VehicleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/vehicle")
public class VehicleController {

    @Autowired private VehicleRepo vehicleRepo;

    @PostMapping("/add")
    public String addVehicle(@RequestBody Vehicle vehicle){
        vehicleRepo.save(vehicle);
        return "Success";
    }

    @GetMapping("/all")
    public Collection<Vehicle> getAll()
    {
        return vehicleRepo.findAll();
    }
}
