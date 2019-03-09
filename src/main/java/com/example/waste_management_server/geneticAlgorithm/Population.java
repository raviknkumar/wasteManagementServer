package com.example.waste_management_server.geneticAlgorithm;

import com.example.waste_management_server.entity.Dustbin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Population {

    private List<Individual> individuals;
    private int populationSize;

    public Population(int populationSize, int numberOfVehicles, List<Dustbin> dustbins, ArrayList<Integer[]> routesGenerated)
    {
        individuals = new ArrayList<>();
        this.populationSize = populationSize;
        Individual.dustbins = dustbins;
        for (int i = 0; i < populationSize; i++)
        {
            Individual individual = new Individual(numberOfVehicles,routesGenerated);
            individuals.add(individual);
        }
    }

    // Saves a Individual
    public void saveIndividual(int index, Individual individual)
    {
        individuals.set(index,individual) ;
    }

    // returns Individual1 at a given position
    public Individual getIndividual(int index)
    {
        return individuals.get(index);
    }

    public Individual getFittest()
    {
        Individual fittest = individuals.get(0);
        for (int i = 1; i < getSize(); i++)
        {
            if (fittest.getFitness() <= individuals.get(i).getFitness())
            {
                fittest = individuals.get(i);
            }
        }
        return fittest;
    }

    public Individual getSecondFittest()
    {
        Individual fittest = getFittest();

        List<Double> fitnesses = individuals.stream().map(Individual::getFitness).sorted().collect(Collectors.toList());
        double secondBest=0;
        if(fitnesses.size()>=1)
         secondBest = fitnesses.get(1);
        for(int i=0;i<getSize();i++)
        {
            if(individuals.get(i).getFitness()==secondBest)
                return individuals.get(i);
        }
        //dummy return
        return individuals.get(0);
    }

    public int getSize()
    {
        return populationSize;
    }

    public String display()
    {
        String s="";
        for(Individual individual:individuals)
            s+="Individual :"+ individual+"\t"
                    +"fitness: "+ individual.getFitness()+"\n";
        return s;
    }

}
