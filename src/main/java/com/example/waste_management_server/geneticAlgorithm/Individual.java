package com.example.waste_management_server.geneticAlgorithm;

import com.example.waste_management_server.entity.Dustbin;
import lombok.Data;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Data
public class Individual {

    private int numberOfVehicles;
    //fitness ->getFitness()
    //distance ->getFitness()
    private int []path;
    private ArrayList<Integer[]> initialPath;
    private List<Dustbin> dustbins;

    public double tourProbability ;
    Random random = new Random();

    public Individual()
    {
        initialPath = new ArrayList<>();
        path = new int[initialPath.size()];
        dustbins = new ArrayList<>();
    }

    public Individual(int numberOfVehicles, List<Dustbin> dustbins, ArrayList<Integer[]> initialPath)
    {
        this.numberOfVehicles = numberOfVehicles;
        this.initialPath = initialPath;
        this.dustbins = dustbins;
        path = new int[initialPath.size()];
    }

    // Creates a random individual
    public void initializeIndividualRandomly()
    {
        for(int i=0;i<path.length;i++)
            path[i] = random.nextInt(numberOfVehicles);
    }

    public void setTourProbability(double total, double fitness)
    {
        this.tourProbability = fitness/total;
    }

    public int getVehicleAtPath(int i)
    {
        return path[i];
    }

    public void setVehicleAtPath(int i, int j)
    {
        path[i]=j;
    }

    private static double getDistance(Dustbin dustbin1, Dustbin dustbin2)
    {
        double x1=dustbin1.getLatitude();
        double y1=dustbin1.getLongitude();
        double x2=dustbin2.getLatitude();
        double y2=dustbin2.getLongitude();

        double a2 = 0;
        double b2 = 0;
        a2 = Math.pow(x2-x1, 2);
        b2 = Math.pow(y2-y1, 2);
        return Math.sqrt(a2 + b2);
    }

    public double getFitness()
    {
        double totalCost=0;
        for(int i=0;i<numberOfVehicles;i++)
            for (int j = 0; j < this.path.length; j++)
              if(i==path[j])
                  totalCost+= getPathFitness(initialPath.get(j));
        return totalCost;
    }

    public double getPathFitness(int path)
    {
        Integer[] truckPath = initialPath.get(path);
        double totalDistanceTravelled = 0;
        int from=-1,to=-1;
        for(int i=0;i<truckPath.length-1;i++) {
            from = truckPath[i];
            to=truckPath[i+1];
            totalDistanceTravelled += getDistance(dustbins.get(from),dustbins.get(to));
        }
        return totalDistanceTravelled;
    }

    public double getPathFitness(Integer[] truckPath)
    {
        double totalDistanceTravelled = 0;
        int from=-1,to=-1;
        for(int i=0;i<truckPath.length-1;i++) {
            from = truckPath[i];
            to=truckPath[i+1];
            totalDistanceTravelled += getDistance(dustbins.get(from),dustbins.get(to));
        }
        return totalDistanceTravelled;
    }

    public int getSize()
    {
        return path.length;
    }

    @Override
    public String toString()
    {
        return Arrays.toString(path);
    }
}
