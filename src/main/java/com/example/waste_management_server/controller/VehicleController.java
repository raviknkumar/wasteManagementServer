package com.example.waste_management_server.controller;

import com.example.waste_management_server.converter.VehicleConverter;
import com.example.waste_management_server.model.VehicleDto;
import com.example.waste_management_server.repository.VehicleRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    @Autowired private VehicleRepo vehicleRepo;
    @Autowired private VehicleConverter vehicleConverter;

    @GetMapping("/getRoutes")
    public List<VehicleDto> getRoutes(@RequestParam String date)
    {
        return vehicleConverter.convertEntityToModel(vehicleRepo.findByDate(date));
    }

    @PostMapping("/add")
    public String add(@RequestBody VehicleDto vehicleDto)
    {
        vehicleRepo.save(vehicleConverter.convertModelToEntity(vehicleDto));
        return "Success";
    }

    @GetMapping("/all")
    public List<VehicleDto> all(){
        return vehicleConverter.convertEntityToModel(vehicleRepo.findAll());
    }
}
