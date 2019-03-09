package com.example.waste_management_server.controller;

import com.example.waste_management_server.converter.VehicleConverter;
import com.example.waste_management_server.entity.Depot;
import com.example.waste_management_server.entity.Dustbin;
import com.example.waste_management_server.entity.VehicleDto;
import com.example.waste_management_server.geneticAlgorithm.GeneticAlgorithm;
import com.example.waste_management_server.geneticAlgorithm.Individual;
import com.example.waste_management_server.geneticAlgorithm.Population;
import com.example.waste_management_server.repository.DustbinRepo;
import com.example.waste_management_server.repository.VehicleRepo;
import com.example.waste_management_server.service.GenerateInputFile;
import com.example.waste_management_server.service.Solver;
import com.example.waste_management_server.test.IndividualTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/dustbin")
public class DustbinController {

    @Autowired private DustbinRepo dustbinRepo;
    @Autowired private GenerateInputFile generateInputFile;
    @Autowired private Solver solver;
    @Autowired private Depot depot;
    @Autowired private VehicleRepo vehicleRepo;
    @Autowired private VehicleConverter vehicleConverter;
    @Autowired private IndividualTest individualTest;


    @PostMapping("/add")
    public String addDustbin(@RequestBody Dustbin dustbin)
    {
        String x = dustbin.getDate();
        dustbinRepo.save(dustbin);
        return   "Successs";
    }

    @GetMapping("/all")
    public List<Dustbin> getAll()
    {
        return dustbinRepo.findAll(Sort.by("dustbinId"));
    }

    @GetMapping("/findByDate")
    public List<Dustbin> getById(@RequestParam String date)
    {
        return dustbinRepo.findByDate(date);
    }

    @GetMapping("/generateInputFile")
    public String generateInputFile(@RequestParam String date,@RequestParam Integer vehicleCapacity) throws FileNotFoundException {
        generateInputFile.generate(date,vehicleCapacity);
        return "Success";
    }

    @GetMapping("/solve")
    public List<VehicleDto>  solveCvrpProblem(/*String date*/) throws IOException {

        String date = "2019-03-06";
        int numberOfVehicles = 5;
        int populationSize =10;
        int numberOfRounds = 10;
        ArrayList<Integer[]> routesGenerated = solver.solve();
        double fillAmount = 7;

        List<Dustbin> dustbins = dustbinRepo.findByDateAndFillAmountGreaterThan(date,fillAmount);
        //add depot as 0th bin to dustbin List
        dustbins.add(0,depot);


        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(numberOfVehicles,dustbins,routesGenerated,numberOfRounds);
        Population population = new Population(populationSize,numberOfVehicles,dustbins,routesGenerated);

        ArrayList<Individual> fittestInAllGenarations = new ArrayList<>();
        fittestInAllGenarations.add(population.getFittest());
        log.info("Fittest in Initial Generation{} {}",population.getFittest(),population.getFittest().getTotalDistanceTravelled());


        for(int i=0;i<numberOfRounds;i++)
        {
            geneticAlgorithm.updateMutationRate(i);
            population=geneticAlgorithm.evolvePopulation(population);
            log.info("Fittest in Generation{} {} {}",i,population.getFittest(),population.getFittest().getTotalDistanceTravelled());
            fittestInAllGenarations.add(population.getFittest());
        }

        String o = "";

        ArrayList<ArrayList<String>> list=new ArrayList<>();
        for(int i=0;i<numberOfVehicles;i++)
        {
            ArrayList<String> al=new ArrayList<>();
            list.add(al);
        }

        Individual bestIndividual = fittestInAllGenarations.get(0);
        for (int i = 1; i < fittestInAllGenarations.size(); i++) {
            if (bestIndividual.getFitness() <= fittestInAllGenarations.get(i).getFitness()) {
                bestIndividual = fittestInAllGenarations.get(i);
            }
        }

        System.out.print(Arrays.toString(bestIndividual.getPath()));

        List<VehicleDto> vehicleDtos = new ArrayList<>();
        for(int i=0;i<numberOfVehicles;i++)
            vehicleDtos.add(VehicleDto.builder().vehicleId(i).pathFollowed(new ArrayList<>()).build());

        int bestPath[] = bestIndividual.getPath(); //[0, 3, 2, 1, 4, 4, 3]
        for(int i=0;i< bestPath.length;i++)
        {
            VehicleDto v = vehicleDtos.get(bestPath[i]);
            if(v.getPathFollowed().isEmpty())
                v.setPathFollowed(new ArrayList<>(Arrays.asList(routesGenerated.get(i))));
            else{
                Integer[] arr = routesGenerated.get(i);
                List<Integer> visited = Arrays.asList(Arrays.copyOfRange(arr,1,arr.length));
                v.getPathFollowed().addAll(visited);
            }
        }

        for(int i=0;i<numberOfVehicles;i++) {
            VehicleDto v = vehicleDtos.get(i);
            v.setDate(date);
            v.setTotalDistanceTravelled(bestIndividual.getRouteDistance(v.getPathFollowed().toArray(new Integer[v.getPathFollowed().size()])));
        }

        vehicleConverter.convertModelToEntity(vehicleDtos).forEach(v->vehicleRepo.save(v));
        return vehicleDtos;
    }

    @GetMapping("/testIndividual")
    public String testIndividual() throws IOException {
        String date = "2019-03-06";
        int numberOfVehicles = 5;
        int populationSize =5;
        int numberOfRounds = 3;
        double fillAmount = 7;

        ArrayList<Integer[]> routesGenerated = solver.solve();


        List<Dustbin> byDate = dustbinRepo.findByDateAndFillAmountGreaterThan(date,fillAmount);
                byDate.add(depot);
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(numberOfVehicles,byDate,routesGenerated,numberOfRounds);
        Population population = new Population(populationSize,numberOfVehicles,byDate,routesGenerated);
        log.info("Fittest in Initial Generation{} {}",population.getFittest(),population.getFittest().getTotalDistanceTravelled());

        for(int i=0;i<numberOfRounds;i++)
        {
            geneticAlgorithm.updateMutationRate(i);
            population=geneticAlgorithm.evolvePopulation(population);
            log.info("Fittest in Generation{} {} {}",i,population.getFittest(),population.getFittest().getTotalDistanceTravelled());
        }

        return "Success";
    }

}

