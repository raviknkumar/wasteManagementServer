package com.example.waste_management_server.genetic;

import com.example.waste_management_server.entity.Dustbin;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Data
public class Individual1 {

    private int numberOfVehicles;
    private int []path;
    private List<Dustbin> dustbins;
    private ArrayList<ArrayList<String>> list;
    int vehicleCapacity = 100;

    public double tourProbability ;
    Random random = new Random();

    public Individual1()
    {
        dustbins = new ArrayList<>();
        path = new int[0]; //excluding depot
        list = new ArrayList<>();
        for(int i=0;i<numberOfVehicles;i++)
        {
            ArrayList<String> al=new ArrayList<>();
            list.add(al);
        }
    }

    public Individual1(int numberOfVehicles, List<Dustbin> dustbins)
    {
        this.numberOfVehicles = numberOfVehicles;
        this.dustbins = dustbins;
        path = new int[dustbins.size()];
        list = new ArrayList<>();
        for(int i=0;i<numberOfVehicles;i++)
        {
            ArrayList<String> al=new ArrayList<>();
            list.add(al);
        }

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
        for(int i=0;i<numberOfVehicles;i++){
            list.get(i).clear();
            list.get(i).add("0");
        }

        int prevDustbin=0;
        double cost=0;
        int dustStored[]=new int[numberOfVehicles];
        int v=0;
        for(int vehicle=0;vehicle<numberOfVehicles;vehicle++)
        {
            prevDustbin=0;
            for(int i=1;i<path.length;i++)
            {
                v=path[i];
                if(v==vehicle)
                {
                    if(dustStored[v]+dustbins.get(i).getFillAmount()<=vehicleCapacity)
                    {
                        cost+=getDistance(dustbins.get(prevDustbin),dustbins.get(i));
                        dustStored[v]+=dustbins.get(i).getFillAmount();
                        prevDustbin=i;
                        list.get(v).add(""+i);

                        if(dustStored[v]==vehicleCapacity)
                        {
                            cost+=getDistance(dustbins.get(i),dustbins.get(0));
                            list.get(v).add("0");
                            dustStored[v]=0;
                            prevDustbin=0;
                        }
                    }
                    else
                    {
                        cost+=getDistance(dustbins.get(prevDustbin),dustbins.get(0));
                        dustStored[v]=0;
                        list.get(v).add("0");
                        cost+=getDistance(dustbins.get(0),dustbins.get(i));
                        dustStored[v]+=dustbins.get(i).getFillAmount();
                        list.get(v).add(""+i);
                        prevDustbin=i;
                    }
                }
            }
        }

        for(int i=0;i<numberOfVehicles;i++)
        {

            int ind=Integer.parseInt(list.get(i).get(list.get(i).size()-1));
            if(ind!=0)
            {
                cost+=getDistance(dustbins.get(ind),dustbins.get(0));
                list.get(i).add("0");
            }
        }

        return cost;
    }
    

    /*private double getPathFitness(int path)
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

    private double getPathFitness(Integer[] truckPath)
    {
        double totalDistanceTravelled = 0;
        int from=-1,to=-1;
        for(int i=0;i<truckPath.length-1;i++) {
            from = truckPath[i];
            to=truckPath[i+1];
            totalDistanceTravelled += getDistance(dustbins.get(from),dustbins.get(to));
        }
        return totalDistanceTravelled;
    }*/

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
