package com.example.waste_management_server.converter;

import com.example.waste_management_server.entity.Vehicle;
import com.example.waste_management_server.model.VehicleDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleConverter {

    public Vehicle convertModelToEntity(VehicleDto vehicleDto)
    {
        return Vehicle.builder()
                .vehicleId(vehicleDto.getVehicleId())
                .date(vehicleDto.getDate())
                .pathFollowed(vehicleDto.getPathFollowed().toString())
                .totalDistanceTravelled(vehicleDto.getTotalDistanceTravelled())
                .build();
    }

    public VehicleDto convertEntityToModel(Vehicle vehicle)
    {
        VehicleDto vehicleDto = VehicleDto.builder()
                .vehicleId(vehicle.getVehicleId())
                .date(vehicle.getDate())
                .totalDistanceTravelled(vehicle.getTotalDistanceTravelled())
                .build();

        String path = vehicle.getPathFollowed().substring(1,vehicle.getPathFollowed().length()-1);
        List<Integer> pathTravelled =new ArrayList<>();
        pathTravelled = Arrays.stream(path.split(",")).map(d_Id -> Integer.parseInt(d_Id.trim())).collect(Collectors.toList());
        vehicleDto.setPathFollowed(pathTravelled);
        return vehicleDto;
    }

    public List<Vehicle> convertModelToEntity(List<VehicleDto> vehicleDtos)
    {
        List<Vehicle> vehicles = new ArrayList<>();
        vehicleDtos.forEach(v->vehicles.add(convertModelToEntity(v)));
        return vehicles;
    }

    public List<VehicleDto> convertEntityToModel(List<Vehicle> vehicles)
    {
        List<VehicleDto> vehicleDtos = new ArrayList<>();
        vehicles.forEach(v->vehicleDtos.add(convertEntityToModel(v)));
        return  vehicleDtos;
    }
}
