package com.example.waste_management_server.controller;

import com.example.waste_management_server.entity.Depot;
import com.example.waste_management_server.entity.Dustbin;
import com.example.waste_management_server.entity.Vehicle;
import com.example.waste_management_server.geneticAlgorithm.GeneticAlgorithm;
import com.example.waste_management_server.geneticAlgorithm.Individual;
import com.example.waste_management_server.geneticAlgorithm.Population;
import com.example.waste_management_server.repository.DustbinRepo;
import com.example.waste_management_server.service.GenerateInputFile;
import com.example.waste_management_server.service.Solver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/dustbin")
public class DustbinController {

    @Autowired private DustbinRepo dustbinRepo;
    @Autowired private GenerateInputFile generateInputFile;
    @Autowired private Solver solver;
    @Autowired private Depot depot;


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
    public List<Vehicle>  solveCvrpProblem(/*String date*/) throws IOException {

        String date = "2019-03-06";
        int numberOfVehicles = 5;
        int populationSize =10;
        int numberOfRounds = 10;
        ArrayList<Integer[]> initialPath = solver.solve();
        List<Dustbin> dustbins = dustbinRepo.findByDate(date);
        //add depot as 0th bin to dustbin List
        dustbins.add(0,depot);
        //Individual1 individual = new Individual1(2,dustbins ,initalPath);
        /*individual.initializeIndividualRandomly();
        return ""+individual.getFitness();*/

        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(numberOfVehicles,dustbins,initialPath);
        Population population = new Population(populationSize,numberOfVehicles,dustbins,initialPath);
        ArrayList<Individual> individuals = new ArrayList<>();

        for(int i=0;i<numberOfRounds;i++) {
            individuals.add(population.getFittest());
            population = geneticAlgorithm.evolvePopulation(population);
        }
        String o = "";
        for(Individual i:individuals)
            o+=i.getFitness()+" ";

        ArrayList<ArrayList<String>> list=new ArrayList<>();
        for(int i=0;i<numberOfVehicles;i++)
        {
            ArrayList<String> al=new ArrayList<>();
            list.add(al);
        }

        Individual bestIndividual = individuals.get(0);
        System.out.print(Arrays.toString(bestIndividual.getPath()));

        List<Vehicle> vehicles = new ArrayList<>();
        for(int i=0;i<numberOfVehicles;i++)
            vehicles.add(Vehicle.builder().vehicleId(i).pathFollowed(new ArrayList<>()).build());

        int bestPath[] = bestIndividual.getPath(); //[0, 3, 2, 1, 4, 4, 3]
        for(int i=0;i< bestPath.length;i++)
        {
            Vehicle v = vehicles.get(bestPath[i]);
            if(v.getPathFollowed().isEmpty())
                v.setPathFollowed(new ArrayList<>(Arrays.asList(initialPath.get(i))));
            else{
                Integer[] arr = initialPath.get(i);
                List<Integer> visited = Arrays.asList(Arrays.copyOfRange(arr,1,arr.length));
                v.getPathFollowed().addAll(visited);
            }
        }

        for(int i=0;i<numberOfVehicles;i++) {
            Vehicle v = vehicles.get(i);
            v.setTotalDistanceTravelled(bestIndividual.getPathFitness(v.getPathFollowed().toArray(new Integer[v.getPathFollowed().size()])));
        }

        return vehicles;
    }

}

