package com.example.waste_management_server.repository;

import com.example.waste_management_server.entity.User;

public interface UserRepo extends BaseRepository<User,Integer> {

    User findByEmail(String email);
}
