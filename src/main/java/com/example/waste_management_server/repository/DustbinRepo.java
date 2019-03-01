package com.example.waste_management_server.repository;

import com.example.waste_management_server.entity.Dustbin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DustbinRepo extends JpaRepository<Dustbin,Integer> {
    List<Dustbin> findByDate(String date);
}
