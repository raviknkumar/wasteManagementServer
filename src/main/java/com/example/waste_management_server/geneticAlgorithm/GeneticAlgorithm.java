package com.example.waste_management_server.geneticAlgorithm;

import com.example.waste_management_server.entity.Dustbin;

import java.util.ArrayList;
import java.util.List;

public class GeneticAlgorithm
{
    /* GeneticAlgorithm1 parameters */
    private static final double mutationRate = 0.005;
    private int numberOfVehicles;
    private List<Dustbin> dustbins;
    private ArrayList<Integer[]> initialPath;

    public GeneticAlgorithm(int numberOfVehicles, List<Dustbin> dustbins, ArrayList<Integer[]> initialPath)
    {
        this.numberOfVehicles = numberOfVehicles;
        this.dustbins = dustbins;
        this.initialPath = initialPath;
    }

    // Evolves a population over one generation
    public Population evolvePopulation(Population pop)
    {

        Population newPopulation = new Population(pop.getSize(), numberOfVehicles,dustbins,initialPath);

        newPopulation.saveIndividual(0,pop.getFittest());               //elitism setting first and last
        newPopulation.saveIndividual(pop.getSize()-1,pop.getFittest()); //elements in population to best in that generation

        for(int i=0; i<pop.getSize(); i++)
        {
            Individual temp = null;
            temp = rouletteWheelSelection(pop);
            newPopulation.saveIndividual(i,temp);
        }
        newPopulation.saveIndividual(0,pop.getFittest());         //elitism as above
        newPopulation.saveIndividual(pop.getSize()-1,pop.getFittest());

        System.out.println("\n Selected Population1 is \n"+ newPopulation.toString());


        Individual parent1 = null;
        Individual parent2 = null;

        for (int i = 1; i < pop.getSize()-1; i++)
        {
            if(i%2==1)
            {
                parent1 = newPopulation.getIndividual(i);
                parent2 = newPopulation.getIndividual(i+1);
            }

            System.out.println("\n The parents are \n" + parent1.toString() + " and \n " + parent2.toString());

            // Crossover parents
            Individual child = crossover(parent1, parent2);

            System.out.println("\n The Child is \n" + child.toString());

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
    public Individual crossover(Individual parent1, Individual parent2)
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

        Individual individual1 = new Individual(numberOfVehicles,dustbins,initialPath);
        individual1.setPath(child1);

        Individual individual2 = new Individual(numberOfVehicles,dustbins,initialPath);
        individual2.setPath(child2);

        return individual1.getFitness()>individual2.getFitness() ? individual1 :individual2;

    }

    // Mutate a tour using swap/shuffle mutation
    private void mutate(Individual individual)
    {
        if(Math.random() < mutationRate) {
            int i = (int) (Math.random() * individual.getSize());
            int j = (int) (Math.random() * individual.getSize());

            int v1 = individual.getVehicleAtPath(i);
            int v2 = individual.getVehicleAtPath(j);

            individual.setVehicleAtPath(i, v2);
            individual.setVehicleAtPath(j, v1);
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