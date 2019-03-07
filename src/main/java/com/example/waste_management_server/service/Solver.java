package com.example.waste_management_server.service;

import com.java.cvrpSolver.vrp.problem.CVRPProblem;
import com.java.cvrpSolver.vrp.problem.CVRPSolution;
import com.java.cvrpSolver.vrp.problem.model.Route;
import com.java.cvrpSolver.vrp.problem.model.Vehicle;
import com.java.cvrpSolver.vrp.solver.CVRPSolver;
import com.java.cvrpSolver.vrp.solver.initializers.BasicInitializer;
import com.java.cvrpSolver.vrp.solver.initializers.Initializer;
import com.java.cvrpSolver.vrp.solver.strategies.SimpleStrategy;
import com.java.cvrpSolver.vrp.solver.strategies.Strategy;
import com.java.cvrpSolver.vrp.utils.loaders.FileLoader;
import com.java.cvrpSolver.vrp.utils.loaders.Loader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.java.cvrpSolver.vrp.solver.strategies.optimizers.InterRouteRelocateOptimizer.RelocateOption;
import static com.java.cvrpSolver.vrp.solver.strategies.optimizers.IntraRouteTwoOptOptimizer.TwoOptOption;

@Service
public class Solver {

    @Autowired private InputFileConfiguration inputFileConfiguration;
    public ArrayList<Integer[]> solve() throws IOException {

        File inputFile= new File(inputFileConfiguration.getInputFileAbsoluteUrl());
        System.out.println(inputFileConfiguration.getInputFileAbsoluteUrl());

        Loader fileLoader = FileLoader.getInstance();

        CVRPProblem cvrpProblem = fileLoader.load(inputFile);
        Initializer problemInitializer = new BasicInitializer();
        Strategy problemStrategy = new SimpleStrategy(
                cvrpProblem,
                TwoOptOption.FIRST_IMPROVEMENT,
                RelocateOption.BEST_IMPROVEMENT,
                false
        );

        CVRPSolver cvrpSolver = new CVRPSolver(
                problemInitializer,
                problemStrategy
        );

        CVRPSolution cvrpSolution = cvrpSolver.solve(cvrpProblem);
        System.out.println("instance: "+inputFile);
        System.out.println("number of customers: "+cvrpProblem.getDustbins().size());
        System.out.println("vehicle capacity: "+cvrpProblem.getVehicleCapacity());
        System.out.println("solution: \n"+cvrpSolution.toString(cvrpProblem.getCostMatrix()));
        System.out.println("cost: "+cvrpSolution.cost(cvrpProblem.getCostMatrix()));

        List<Vehicle> vehicles = cvrpSolution.getVehicles();
        ArrayList<Integer[]> initialPath = new ArrayList<>();
        for(Vehicle vehicle: vehicles)
        {
            Route route = vehicle.getRoute();
            Integer path[] = route.getRoute().stream().map(node->node.getId()).toArray(Integer[]::new);
            initialPath.add(path);
        }

        return initialPath;
    }
}
