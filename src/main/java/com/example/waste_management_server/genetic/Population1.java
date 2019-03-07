package com.example.waste_management_server.genetic;

import com.example.waste_management_server.entity.Dustbin;

import java.util.ArrayList;
import java.util.List;


public class Population1 {

    private List<Individual1> population = new ArrayList<>();
    private int populationSize;


    // Constructs a blank tour
    public Population1(int populationSize, int numberOfVehicles, List<Dustbin> dustbins)
    {
        this.populationSize = populationSize;
        for (int i = 0; i < populationSize; i++)
        {
            Individual1 individual1 = new Individual1(numberOfVehicles,dustbins);
            individual1.initializeIndividualRandomly();
            population.add(individual1);
        }
    }

    // Saves a Individual1
    public void saveIndividual(int index, Individual1 individual1)
    {
        population.set(index, individual1) ;
    }

    // returns Individual1 at a given position
    public Individual1 getIndividual(int index)
    {
        return population.get(index);
    }

    public Individual1 getFittest()
    {
        Individual1 fittest = population.get(0);
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
