package com.example.user_manager;

public class UserNotFoundException extends RuntimeException{
    UserNotFoundException(Long id) {
        super("Could not find user " + id);
    }
}