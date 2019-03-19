package com.example.waste_management_server.repository;

import com.example.waste_management_server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Integer> {

    User findByEmail(String email);
}
