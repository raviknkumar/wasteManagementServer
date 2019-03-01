package com.example.waste_management_server.controller;

import com.example.waste_management_server.entity.Dustbin;
import com.example.waste_management_server.repository.DustbinRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/dustbin")
public class DustbinController {

    @Autowired private DustbinRepo dustbinRepo;

    @PostMapping("/add")
    public String addDustbin(@RequestBody Dustbin dustbin)
    {
        String x = dustbin.getDate();
        dustbinRepo.save(dustbin);
        return   "Successs";
    }

    @GetMapping("/all")
    public List<Dustbin> getAll()
    {
        return dustbinRepo.findAll(Sort.by("dustbinId"));
    }

    @GetMapping("/findByDate")
    public List<Dustbin> getById(@RequestParam String date)
    {
        return dustbinRepo.findByDate(date);
    }


}

