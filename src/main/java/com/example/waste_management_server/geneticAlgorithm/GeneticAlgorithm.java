package com.example.waste_management_server.geneticAlgorithm;

import com.example.waste_management_server.entity.Dustbin;
import com.java.cvrpSolver.vrp.utils.collections.Pair;

import java.util.*;

public class GeneticAlgorithm
{
    /* GeneticAlgorithm parameters */
    private double mutationRate;
    private int numberOfVehicles;
    private List<Dustbin> dustbins;
    private ArrayList<Integer[]> routesGenerated;
    private int totalIterations ;

    public GeneticAlgorithm(int numberOfVehicles, List<Dustbin> dustbins, ArrayList<Integer[]> routesGenerated,int totalIterations)
    {
        mutationRate=0.6;
        this.numberOfVehicles = numberOfVehicles;
        this.dustbins = dustbins;
        this.routesGenerated = routesGenerated;
        this.totalIterations = totalIterations;
    }

    public double updateMutationRate(int iterationNumber)
    {
        return mutationRate*(totalIterations-iterationNumber)/iterationNumber;
    }

    // Evolves a population over one generation
    public Population evolvePopulation(Population pop)
    {

        Population newPopulation = new Population(pop.getSize(), numberOfVehicles,dustbins, routesGenerated);

        newPopulation.saveIndividual(0,pop.getFittest());               //elitism setting first and last
        newPopulation.saveIndividual(pop.getSize()-1,pop.getSecondFittest()); //elements in population to best in that generation

        for(int i=0; i<pop.getSize(); i++)
        {
            Individual temp = null;
            temp = rouletteWheelSelection(pop);
            newPopulation.saveIndividual(i,temp);
        }

        newPopulation.saveIndividual(0,pop.getFittest());         //elitism as above
        newPopulation.saveIndividual(pop.getSize()-1,pop.getFittest());

        // System.out.println("\n Selected Population1 is \n"+ newPopulation.display());


        Individual parent1 = null;
        Individual parent2 = null;

        for (int i = 1; i < pop.getSize()-1; i++)
        {
            if(i%2==1)
            {
                parent1 = newPopulation.getIndividual(i);
                parent2 = newPopulation.getIndividual(i+1);
            }

            System.out.println("\n The parents are \n" + parent1 + " and \n " + parent2);

            // Crossover parents
            Individual child = crossover(parent1, parent2);

            System.out.println("\n The Child is \n" + child);

            // Add child to new population
            newPopulation.saveIndividual(i, child);
        }

        // Mutate the new population
        for (int i = 0; i <pop.getSize(); i++)
        {
            mutate(newPopulation.getIndividual(i));
        }

        return newPopulation;
    }

    // Applies crossover to a set of parents and creates offspring
    // uniform crossover
    public Individual crossover(Individual parent1, Individual parent2)
    {
        Set<Integer> vehiclesCoveringRoutes = new HashSet<>();
        Arrays.stream(parent1.getPath()).forEach(vehiclesCoveringRoutes::add);
        Arrays.stream(parent2.getPath()).forEach(vehiclesCoveringRoutes::add);

        Pair<Integer,Integer> minMaxForParent1 = parent1.findMinMaxTrucks();
        Pair<Integer,Integer> minMaxForParent2 = parent2.findMinMaxTrucks();

        //longestRouteForParent1 -> MinRouteForParent2
        Individual child = new Individual(numberOfVehicles,routesGenerated);
        int[] path = new int[routesGenerated.size()];
        for(int i=0;i<path.length;i++)
            path[i]=-1;

        for(int i=0;i<path.length;i++)
        {
            if(parent1.getVehicleAtPath(i)==parent2.getVehicleAtPath(i))
                path[i]=parent1.getVehicleAtPath(i);
        }

//        int numChilds = parent1.getVehicleVsRoutesGeneratedMap().get(minMaxForParent1.getSecond()).size();
//        generate children with numChilds and replace Each position of maxTruckP1 with minTruckP2
//        evaluate the best child and return
        System.out.println("Max In P1:"+minMaxForParent1.getSecond());
        System.out.println("Min In P2:"+minMaxForParent2.getFirst());
        System.out.println("Min In P1:"+minMaxForParent1.getFirst());
        System.out.println("Max In P2:"+minMaxForParent2.getSecond());

        if(!minMaxForParent1.getSecond().equals(minMaxForParent2.getFirst()))
            path[parent1.getLongestRouteForVehicle(minMaxForParent1.getSecond())]=minMaxForParent2.getFirst();
        else if(!minMaxForParent2.getSecond().equals(minMaxForParent1.getFirst()))
            path[parent2.getLongestRouteForVehicle(minMaxForParent1.getSecond())]=minMaxForParent1.getFirst();

        //As of now
        for(int i=0;i<path.length;i++)
            if(path[i]==-1)
                path[i]=parent1.getVehicleAtPath(i);

        child.setPath(path);

        return child;
        /*// Create new child tour
        int[] child1 = new int[parent1.getSize()];
        int[] child2 = new int[parent1.getSize()];

        // Get start and end sub tour positions for parent1's tour
        int startPos = (int) (Math.random() * parent1.getSize());
        int endPos = (int) (Math.random() * parent1.getSize());
        if(startPos>endPos)
        {
            int t=endPos;
            endPos=startPos;
            startPos=t;
        }
        for(int i=0;i<parent1.getSize();i++)
        {
            if(i<startPos || i>endPos) {
                child1[i] = parent1.getPath()[i];
                child2[i] = parent2.getPath()[i];
            }
            else {
                child1[i] = parent2.getPath()[i];
                child2[i] = parent1.getPath()[i];
            }
        }

        Individual individual1 = new Individual(numberOfVehicles, routesGenerated);
        individual1.setPath(child1);
        Individual individual2 = new Individual(numberOfVehicles, routesGenerated);
        individual2.setPath(child2);
        return individual1.getFitness() < individual2.getFitness() ? individual1 :individual2;*/

    }

    // Mutate a tour using swap/shuffle mutation
    private void mutate(Individual individual)
    {
        if(Math.random() < mutationRate) {

            Pair<Integer,Integer> minMaxTrucks = individual.findMinMaxTrucks();
            int minFrequentTruck = minMaxTrucks.getFirst();
            int maxFrequentTruck = minMaxTrucks.getSecond();

            if(individual.distanceTravelledByVehicle(minFrequentTruck)<individual.distanceTravelledByVehicle(maxFrequentTruck))
            {
                //set the longest route of maxFrequentTruck to minFreqTruck
                individual.setVehicleAtPath(minFrequentTruck,individual.getLongestRouteForVehicle(maxFrequentTruck));
            }
        }
    }

    public Individual rouletteWheelSelection(Population p)
    {
        double totalSum = 0.0;
        Individual t = new Individual();

        for(int i=0; i<p.getSize(); i++)
        {
            totalSum += p.getIndividual(i).getFitness();
        }

        for(int i=0; i<p.getSize(); i++)
        {
            p.getIndividual(i).setTourProbability(totalSum, p.getIndividual(i).getFitness());
        }

        double partialSum = 0;
        double roulette = 0;
        roulette = (double)(Math.random());

        for(int i=0; i<p.getSize(); i++)
        {
            partialSum+=p.getIndividual(i).tourProbability;

            if(partialSum>=roulette)
            {
                t = p.getIndividual(i);
                break;
            }
        }

        return t;
    }


}