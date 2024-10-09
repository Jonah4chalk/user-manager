package com.example.user_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.user_manager.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
}
