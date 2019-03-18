package com.example.waste_management_server.service;

import com.example.waste_management_server.entity.Depot;
import com.example.waste_management_server.entity.Dustbin;
import com.example.waste_management_server.repository.DustbinRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class GenerateInputFile {
    @Autowired private InputFileConfiguration inputFileConfiguration;
    @Autowired private DustbinRepo dustbinRepo;
    @Autowired private Depot depot;


    public void generate(String date, int vehicleCapacity) throws FileNotFoundException {
        List<Dustbin> dustbins = dustbinRepo.findByDate(date);
        int numberOfDustbins = dustbins.size();
        int maximumRouteTime = 99999;
        int dropTime = 0;

        double depotXCoOrdinate = depot.getLatitude();
        double depotYCoOrdinate = depot.getLongitude();

        File file = new File(inputFileConfiguration.getInputFileAbsoluteUrl());
        file.getParentFile().mkdirs();
        PrintWriter printWriter = new PrintWriter(file);

        printWriter.println(numberOfDustbins+" "+vehicleCapacity+" "+maximumRouteTime+" "+dropTime);
        printWriter.println(depotXCoOrdinate+" "+depotYCoOrdinate);
        for(Dustbin dustbin:dustbins)
            printWriter.println(dustbin.getLatitude()+" "+dustbin.getLongitude()+" "+dustbin.getFillAmount());

        if(printWriter!=null)
            printWriter.close();

    }
}
