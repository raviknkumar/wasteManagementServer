package com.example.waste_management_server.controller;

import com.example.waste_management_server.converter.VehicleConverter;
import com.example.waste_management_server.entity.Depot;
import com.example.waste_management_server.entity.Dustbin;
import com.example.waste_management_server.entity.Vehicle;
import com.example.waste_management_server.model.StatisticsInfoDto;
import com.example.waste_management_server.model.VehicleDto;
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
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
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
    public String generateInputFile(@RequestParam String date,@RequestParam Integer vehicleCapacity/*,@RequestParam Double wasteLimit*/) throws FileNotFoundException {
        generateInputFile.generate(date,vehicleCapacity/*,wasteLimit*/);
        return "Success";
    }

    @GetMapping("/solve")
    public String  solveCvrpProblem(@RequestParam String date, @RequestParam Integer numberOfVehicles,@RequestParam Integer vehicleCapacity) throws IOException {

       generateInputFile.generate(date,vehicleCapacity);
        int populationSize =10;
        int numberOfRounds = 10;
        ArrayList<Integer[]> routesGenerated = solver.solve();
//        double fillAmount = 7;

        List<Dustbin> dustbins = dustbinRepo.findByDate(date);
        //add depot as 0th bin to dustbin List
        dustbins.add(0,depot);


        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(numberOfVehicles,dustbins,routesGenerated,numberOfRounds);
        Map<Integer,Integer> indexVsDustbinId = new HashMap<>();

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
        return "Generated Routes Succesfully";
    }

    @GetMapping("/testIndividual")
    public String testIndividual() throws IOException {
        String date = "2019-03-06";
        int numberOfVehicles = 5;
        int populationSize =10;
        int numberOfRounds = 10;
        ArrayList<Integer[]> routesGenerated = solver.solve();
        double fillAmount = 7;

        List<Dustbin> dustbins = dustbinRepo.findByDateAndFillAmountGreaterThanEqual(date,fillAmount);
        //add depot as 0th bin to dustbin List
        dustbins.add(0,depot);


        Individual.dustbins = dustbins;
        Individual parent1 = new Individual(numberOfVehicles,routesGenerated);
        parent1.setPath(new int[]{0, 1, 2});
        Individual parent2 = new Individual(numberOfVehicles,routesGenerated);
        parent2.setPath(new int[]{3, 0, 0});

        /*Max In P1:1
        Min In P2:1
        Min In P1:3
        Max In P2:0*/

        System.out.println(parent1.getLongestRouteForVehicle(1));
        //System.out.println(parent2.getLongestRouteForVehicle(1));
        //System.out.println(parent1.getLongestRouteForVehicle(3));
        System.out.println(parent2.getLongestRouteForVehicle(0));

        /*if(!minMaxForParent1.getSecond().equals(minMaxForParent2.getFirst()))
            path[parent1.getLongestRouteForVehicle(minMaxForParent1.getSecond())]=minMaxForParent2.getFirst();
        else if(!minMaxForParent2.getSecond().equals(minMaxForParent1.getFirst()))
            path[parent2.getLongestRouteForVehicle(minMaxForParent1.getSecond())]=minMaxForParent1.getFirst();*/

        return "Success";
    }

    @GetMapping("/stats")
    public StatisticsInfoDto getStatsData(@RequestParam String date){
        StatisticsInfoDto statisticsInfoDto = StatisticsInfoDto.builder().build();
        List<Dustbin> dustbins = dustbinRepo.findByDate(date);
        statisticsInfoDto.setNumberOfDustbins(dustbins.size());
        statisticsInfoDto.setDustbinIds(dustbins.stream().map(Dustbin::getDustbinId).collect(Collectors.toList()));
        statisticsInfoDto.setFillAmounts(dustbins.stream().map(Dustbin::getFillAmount).collect(Collectors.toList()));
        statisticsInfoDto.setTotalFillAmount(dustbins.stream().mapToDouble(Dustbin::getFillAmount).sum());

        Collection<Vehicle> vehicles = vehicleRepo.findByDate(date);
        double totalDistance= vehicles.stream().mapToDouble(Vehicle::getTotalDistanceTravelled).sum();
        statisticsInfoDto.setTotalDistanceTravlled(totalDistance);

        return  statisticsInfoDto;
    }

    @GetMapping("/testSolve")
    public String testSolve()
    {
        return null;
    }

}
