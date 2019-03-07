package com.example.waste_management_server.genetic;

import com.example.waste_management_server.entity.Dustbin;

import java.util.ArrayList;
import java.util.List;

public class GeneticAlgorithm1
{
    /* GeneticAlgorithm1 parameters */
    private static final double mutationRate = 0.005;
    private int numberOfVehicles;
    private List<Dustbin> dustbins;

    public GeneticAlgorithm1(int numberOfVehicles, List<Dustbin> dustbins)
    {
        this.numberOfVehicles = numberOfVehicles;
        this.dustbins = dustbins;
    }

    // Evolves a population over one generation
    public Population1 evolvePopulation(Population1 pop)
    {

        Population1 newPopulation1 = new Population1(pop.getSize(), numberOfVehicles,dustbins);

        newPopulation1.saveIndividual(0,pop.getFittest());               //elitism setting first and last
        newPopulation1.saveIndividual(pop.getSize()-1,pop.getFittest()); //elements in population to best in that generation

        for(int i=0; i<pop.getSize(); i++)
        {
            Individual1 temp = null;
            temp = rouletteWheelSelection(pop);
            newPopulation1.saveIndividual(i,temp);
        }
        newPopulation1.saveIndividual(0,pop.getFittest());         //elitism as above
        newPopulation1.saveIndividual(pop.getSize()-1,pop.getFittest());

        System.out.println("\n Selected Population1 is \n"+ newPopulation1.toString());


        Individual1 parent1 = null;
        Individual1 parent2 = null;

        for (int i = 1; i < pop.getSize()-1; i++)
        {
            if(i%2==1)
            {
                parent1 = newPopulation1.getIndividual(i);
                parent2 = newPopulation1.getIndividual(i+1);
            }

            System.out.println("\n The parents are \n" + parent1.toString() + " and \n " + parent2.toString());

            // Crossover parents
            Individual1 child = crossover(parent1, parent2);

            System.out.println("\n The Child is \n" + child.toString());

            // Add child to new population
            newPopulation1.saveIndividual(i, child);
        }

        // Mutate the new population
        for (int i = 0; i <pop.getSize(); i++)
        {
            mutate(newPopulation1.getIndividual(i));
        }

        return newPopulation1;
    }

    // Applies crossover to a set of parents and creates offspring
    public Individual1 crossover(Individual1 parent1, Individual1 parent2)
    {
        // Create new child tour
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

        Individual1 individual1 = new Individual1(numberOfVehicles,dustbins);
        individual1.setPath(child1);

        Individual1 individual12 = new Individual1(numberOfVehicles,dustbins);
        individual12.setPath(child2);

        return individual1.getFitness()> individual12.getFitness() ? individual1 : individual12;

    }

    // Mutate a tour using swap/shuffle mutation
    private void mutate(Individual1 individual1)
    {
        if(Math.random() < mutationRate) {
            int i = (int) (Math.random() * individual1.getSize());
            int j = (int) (Math.random() * individual1.getSize());

            int v1 = individual1.getVehicleAtPath(i);
            int v2 = individual1.getVehicleAtPath(j);

            individual1.setVehicleAtPath(i, v2);
            individual1.setVehicleAtPath(j, v1);
        }
    }

    public Individual1 rouletteWheelSelection(Population1 p)
    {
        double totalSum = 0.0;
        Individual1 t = new Individual1();

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