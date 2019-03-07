package com.example.waste_management_server.geneticAlgorithm;

import com.example.waste_management_server.entity.Dustbin;

import java.util.ArrayList;
import java.util.List;


public class Population {

    private List<Individual> population = new ArrayList<>();

//    private int numberOfVehicles;
//    private double fitness = 0;
//    private int distance = 0;
    private int populationSize;


    // Constructs a blank tour
    public Population(int populationSize, int numberOfVehicles, List<Dustbin> dustbins, ArrayList<Integer[]> initialPath)
    {
        this.populationSize = populationSize;
        for (int i = 0; i < populationSize; i++)
        {
            Individual individual = new Individual(numberOfVehicles,dustbins,initialPath);
            individual.initializeIndividualRandomly();
            population.add(individual);
        }
    }

    // Saves a Individual1
    public void saveIndividual(int index, Individual individual)
    {
        population.set(index,individual) ;
    }

    // returns Individual1 at a given position
    public Individual getIndividual(int index)
    {
        return population.get(index);
    }

    public Individual getFittest()
    {
        Individual fittest = population.get(0);
        for (int i = 1; i < getSize(); i++)
        {
            if (fittest.getFitness() <= population.get(i).getFitness())
            {
                fittest = population.get(i);
            }
        }
        return fittest;
    }

    public int getSize()
    {
        return populationSize;
    }

    @Override
    public String toString()
    {
        String s="";
        for(int i=0;i<populationSize;i++)
            s+=population.get(i);
        return s;
    }


}
