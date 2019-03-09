package com.example.waste_management_server.test;

import com.example.waste_management_server.entity.Depot;
import com.example.waste_management_server.entity.Dustbin;
import com.example.waste_management_server.geneticAlgorithm.GeneticAlgorithm;
import com.example.waste_management_server.geneticAlgorithm.Individual;
import com.example.waste_management_server.geneticAlgorithm.Population;
import com.example.waste_management_server.repository.DustbinRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class IndividualTest {

    @Autowired private DustbinRepo dustbinRepo;
    @Autowired private Depot depot;

    public boolean testIndividualConstructor(int numVehicles, ArrayList<Integer[]> routesGenerated)
    {
        /*Individual.dustbins = dustbinRepo.findByDate("2019-03-06");
        Individual.dustbins.add(depot);
        Individual individual = Individual.builder()
                .numberOfVehicles(numVehicles)
                .routesGenerated(routesGenerated)
                .build();

        individual.setPath((new int[]{4, 3, 4, 0, 0, 4, 4}));
        // assert condition : IfFalse evaluation
        // assert value >= 20 : " Underweight";

        log.info("Initial Path{}", Arrays.toString(individual.getPath()));
        log.info("InitialMap{}",individual.getVehicleVsRoutesGeneratedMap());
        individual.setVehicleAtPath(1,5);
        log.info("UpdatedPath {}", Arrays.toString(individual.getPath()));
        log.info("UpdatedMap{}",individual.getVehicleVsRoutesGeneratedMap());*/


        /*double d=0;
        for(int i=0;i<numVehicles;i++){
            log.info("V{} {}",i,individual.distanceTravelledByVehicle(i));
            d+=individual.distanceTravelledByVehicle(i);
        }
        log.info("Total Dist:{}",d);

        Pair<Integer, Integer> minMaxTruck = individual.findMinMaxTrucks();
        log.info("Min:{} max:{}",minMaxTruck.getFirst(),minMaxTruck.getSecond());*/

//        log.info("{}",individual.getLongestRouteForVehicle(2));

        return true;
    }

    public boolean testPopulation(int populationSize, int numberOfVehicles, List<Dustbin> dustbins, ArrayList<Integer[]> routesGenerated)
    {
        Population population = new Population(populationSize,numberOfVehicles,dustbins,routesGenerated);
        return true;
    }

    public boolean testGA(int populationSize, int numberOfVehicles, List<Dustbin> dustbins, ArrayList<Integer[]> routesGenerated)
    {
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(numberOfVehicles,dustbins,routesGenerated,3);
        Population population = new Population(populationSize,numberOfVehicles,dustbins,routesGenerated);
        log.info("{}",population);
        log.info("Best{}",population.getFittest());
        log.info("Second Best{}",population.getSecondFittest());
        return true;
    }
}
