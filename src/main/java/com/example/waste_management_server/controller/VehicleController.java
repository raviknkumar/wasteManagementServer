package com.example.waste_management_server.controller;

import com.example.waste_management_server.converter.VehicleConverter;
import com.example.waste_management_server.model.VehicleDto;
import com.example.waste_management_server.repository.VehicleRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
