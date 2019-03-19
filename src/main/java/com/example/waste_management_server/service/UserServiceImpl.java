package com.example.waste_management_server.service;

import com.example.waste_management_server.entity.AppUser;
import com.example.waste_management_server.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl {

    @Autowired private UserRepo userRepo;

    public boolean verifyUser(String userEmail,String password)
    {
        AppUser appUser = userRepo.findByEmail(userEmail);
        if(appUser.getPassword().equals(password))
            return true;
        return false;
    }

    public String saveUser(AppUser appUser)
    {
        userRepo.save(appUser);
        return "Success";
    }

    public boolean validateEmail(AppUser appUser)
    {
        AppUser appUser1 = userRepo.findByEmail(appUser.getEmail());
        if(appUser1 ==null || appUser1.getEmail() == null)
            return true;
        return false;
    }

    public boolean verifyPassword(AppUser appUser)
    {
        AppUser originalAppUser = userRepo.findByEmail(appUser.getEmail());
        if(originalAppUser ==null)
            return false;
        return originalAppUser.getPassword().equals(appUser.getPassword());
    }
}
