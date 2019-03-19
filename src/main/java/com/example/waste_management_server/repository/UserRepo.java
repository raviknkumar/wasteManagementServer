package com.example.waste_management_server.repository;

import com.example.waste_management_server.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<AppUser,Integer> {

    AppUser findByEmail(String email);
}
