package com.example.waste_management_server.controller;

import com.example.waste_management_server.entity.AppUser;
import com.example.waste_management_server.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.waste_management_server.repository.UserRepo;

@RestController
@RequestMapping("/user")
@CrossOrigin(maxAge = 3600)
public class UserController {

    @Autowired private UserRepo userRepo;
    @Autowired private UserServiceImpl userService;

    @PostMapping("/create")
    public String createUser(@RequestBody AppUser appUser)
    {
        return userService.saveUser(appUser);
    }

    @PostMapping("/login")
    public boolean verifyUser(@RequestBody AppUser appUser)
    {
        return userService.verifyUser(appUser.getEmail(), appUser.getPassword());
    }

    @PostMapping("/validateEmail")
    public boolean validateEmail(@RequestBody AppUser appUser)
    {
        return userService.validateEmail(appUser);
    }

    @PostMapping("/verifyPassword")
    public boolean verifyPassword(@RequestBody AppUser appUser)
    {
        return userService.verifyPassword(appUser);
    }

}
