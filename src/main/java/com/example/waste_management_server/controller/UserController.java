package com.example.waste_management_server.controller;

import com.example.waste_management_server.entity.User;
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
    public String createUser(@RequestBody User user)
    {
        return userService.saveUser(user);
    }

    @PostMapping("/login")
    public boolean verifyUser(@RequestBody User user)
    {
        return userService.verifyUser(user.getEmail(),user.getPassword());
    }

    @PostMapping("/validateEmail")
    public boolean validateEmail(@RequestBody User user)
    {
        return userService.validateEmail(user);
    }

    @PostMapping("/verifyPassword")
    public boolean verifyPassword(@RequestBody User user)
    {
        return userService.verifyPassword(user);
    }

}
