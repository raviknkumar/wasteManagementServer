package com.example.waste_management_server.geneticAlgorithm;

import com.example.waste_management_server.entity.Dustbin;
import com.java.cvrpSolver.vrp.utils.collections.Pair;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Builder
@Data
@Slf4j
@AllArgsConstructor
//@Component
public class Individual {

    private int numberOfVehicles;
    private int []path;
    private ArrayList<Integer[]> routesGenerated;
    public static List<Dustbin> dustbins = new ArrayList<>();
    private Map<Integer,List<Integer>> vehicleVsRoutesGeneratedMap;
    private double totalDistanceTravelled;

    public double tourProbability ;
    Random random = new Random();

    //dummy noArgsConstructor
    public Individual()
    {
        routesGenerated = null;
        path = new int[1];
    }

    //testedAllArgsConstructor
    public Individual(int numberOfVehicles, ArrayList<Integer[]> routesGenerated)
    {
        this.numberOfVehicles = numberOfVehicles;
        this.routesGenerated = routesGenerated;
        path = new int[routesGenerated.size()];
        //initialize the path
        if(Math.random()<0.2)
            initializeIndividual();
        else
            initializeIndividualRandomly();
        //to generate a map of vehicles vs routesGenerated it visits
        initializeMap();
    }

    // Creates a individual in roundRobinManner
    public void initializeIndividual()
    {
        for(int i=0;i<path.length;i++)
            path[i] = i%numberOfVehicles;
    }

    public void initializeIndividualRandomly()
    {
        for(int i=0;i<path.length;i++)
            path[i] = random.nextInt(numberOfVehicles);
    }



    /**
     * input: [4, 3, 4, 0, 0, 4, 4]
     * output:{ 0->[3,4], 1->[], 2->[], 3->[1], 4->[0,2,5,6]}
     * path[index] -> VehicleNumber
     * index is routeNumber
     */
    private void initializeMap()
    {
        vehicleVsRoutesGeneratedMap = new HashMap<>();
        for(int i=0;i<numberOfVehicles;i++)
            vehicleVsRoutesGeneratedMap.put(i,new ArrayList<>());


        for (int j = 0; j < path.length; j++) {
            vehicleVsRoutesGeneratedMap.get(path[j]).add(j);
        }

    }

    public void setPath(int[] path) {
        this.path = path;
        initializeMap();
    }

    public void setTourProbability(double total, double fitness)
    {
        this.tourProbability = fitness/total;
    }

    public int getVehicleAtPath(int i)
    {
        return path[i];
    }

    /**
     * @param vehicle
     * @param pathIndex
     */
    public void setVehicleAtPath(int vehicle, int pathIndex)
    {
        try {
            //remove route assigned to previous vehicle
            int previousVehicle = path[pathIndex];
            vehicleVsRoutesGeneratedMap.get(previousVehicle).remove(new Integer(pathIndex));
            //assign route to new Vehicle
            path[pathIndex] = vehicle;
            vehicleVsRoutesGeneratedMap.get(vehicle).add(pathIndex);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage()+Arrays.toString(path)+"\n"+vehicleVsRoutesGeneratedMap
                    +"\nVehicle:"+vehicle+" pathIndex: "+pathIndex);
        }
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
        double[] distanceOfEachVehicle = new double[numberOfVehicles];
        totalDistanceTravelled = getTotalDistanceTravelled();
        double mean = totalDistanceTravelled/numberOfVehicles;

        //mean deviation
        double fitness = 0;
        for(int i=0;i<numberOfVehicles;i++)
            fitness+=Math.abs(mean-distanceOfEachVehicle[i]);

        return fitness;
    }


    /**
     * gives the distance to travel in the route given index of route in routesGenerated
     * @param routeIndex
     * @return
     */
    public double getRouteDistance(int routeIndex)
    {
        Integer[] vehiclePath = routesGenerated.get(routeIndex);
        return getRouteDistance(vehiclePath);
    }

    public double getRouteDistance(Integer[] vehiclePath)
    {
        double totalDistanceTravelled = 0;
        int from=-1,to=-1;
        for(int i=0;i<vehiclePath.length-1;i++) {
            from = vehiclePath[i];
            to=vehiclePath[i+1];
            totalDistanceTravelled += getDistance(dustbins.get(from),dustbins.get(to));
        }
        return totalDistanceTravelled;
    }

    public double distanceTravelledByVehicle(int vehicleNum)
    {
        double distanceTravelled = 0;
        List<Integer> routesCarried = vehicleVsRoutesGeneratedMap.get(vehicleNum);
        if(routesCarried!=null)
            for(Integer routeCarried: routesCarried)
                distanceTravelled+= getRouteDistance(routeCarried);

        return distanceTravelled;
    }

    public int getSize()
    {
        return path.length;
    }

    public Pair<Integer,Integer> findMinMaxTrucks()
    {
        int minFrequentTruck = 0, maxFrequentTruck = 0;

        for(Integer i:vehicleVsRoutesGeneratedMap.keySet())
        {
            if(vehicleVsRoutesGeneratedMap.get(i).size() == vehicleVsRoutesGeneratedMap.get(maxFrequentTruck).size())
            {
                if(distanceTravelledByVehicle(i)>distanceTravelledByVehicle(maxFrequentTruck))
                    maxFrequentTruck=i;
            }

            if(vehicleVsRoutesGeneratedMap.get(i).size() > vehicleVsRoutesGeneratedMap.get(maxFrequentTruck).size())
                maxFrequentTruck=i;
            if(vehicleVsRoutesGeneratedMap.get(i).size() == vehicleVsRoutesGeneratedMap.get(minFrequentTruck).size())
            {
                if(distanceTravelledByVehicle(i) < distanceTravelledByVehicle(minFrequentTruck))
                    minFrequentTruck=i;
            }
            if(vehicleVsRoutesGeneratedMap.get(i).size() < vehicleVsRoutesGeneratedMap.get(minFrequentTruck).size())
                minFrequentTruck=i;
        }
        return new Pair<>(minFrequentTruck,maxFrequentTruck);
    }


    public int getLongestRouteForVehicle(int vehicle)
    {
        List<Double> distanceOfEachRoute=new ArrayList<>();
        try {
            distanceOfEachRoute = vehicleVsRoutesGeneratedMap.get(vehicle).stream().map(routeNumber -> getRouteDistance(routeNumber)).collect(Collectors.toList());
            return distanceOfEachRoute.indexOf(Collections.max(distanceOfEachRoute));
        }
        catch (Exception e)
        {
            System.out.println("vehicle:"+vehicle+" Path:"+path+" distanceOfEachRoute: "+distanceOfEachRoute+"Max: "+Collections.max(distanceOfEachRoute));
        }
        return 0;
    }

    public double getTotalDistanceTravelled()
    {
        double totalDistance = 0;
        for(Integer vehicle:vehicleVsRoutesGeneratedMap.keySet())
            totalDistance+= distanceTravelledByVehicle(vehicle);

        return totalDistance;
    }

    @Override
    public String toString()
    {
        String s=Arrays.toString(path)+"\n";
        for(int i=0;i<numberOfVehicles;i++)
            s+="Vehicle "+i+" "+distanceTravelledByVehicle(i)+"\t";
        s+="\n Total: "+totalDistanceTravelled+"\n";
        return s;
    }

}
