package com.example.waste_management_server.service;

import com.example.waste_management_server.entity.User;
import com.example.waste_management_server.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl {

    @Autowired private UserRepo userRepo;

    public boolean verifyUser(String userEmail,String password)
    {
        User user = userRepo.findByEmail(userEmail);
        if(user.getPassword().equals(password))
            return true;
        return false;
    }

    public String saveUser(User user)
    {
        userRepo.save(user);
        return "Success";
    }

    public boolean validateEmail(User user)
    {
        User user1 = userRepo.findByEmail(user.getEmail());
        if(user1==null || user1.getEmail() == null)
            return true;
        return false;
    }

    public boolean verifyPassword(User user)
    {
        User originalUser = userRepo.findByEmail(user.getEmail());
        if(originalUser==null)
            return false;
        return originalUser.getPassword().equals(user.getPassword());
    }
}
