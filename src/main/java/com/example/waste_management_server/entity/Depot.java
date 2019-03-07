package com.example.waste_management_server.entity;

import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class Depot extends Dustbin  {

    public Depot()
    {
        setDustbinId(0);
        setLatitude(10.0);
        setLongitude(10.0);
    }

}
