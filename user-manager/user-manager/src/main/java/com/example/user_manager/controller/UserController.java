package com.example.user_manager.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.user_manager.entity.User;
import com.example.user_manager.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository usersRepository) {
        this.userRepository = usersRepository;
    }

    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{user_id}")
    public User getUser(@PathVariable Long user_id) {
        return userRepository.findById(user_id).orElseThrow(RuntimeException::new);
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody User user) throws URISyntaxException {
        User createdUser = userRepository.save(user);
        return ResponseEntity.created(new URI("/users/" + Long.toString(createdUser.getId()))).body(createdUser);
    }
    
    @PutMapping("/{user_id}")
    public ResponseEntity updateUser(@PathVariable Long id, @RequestBody User user) {
        User currentUser = userRepository.findById(id).orElseThrow(RuntimeException::new);
        currentUser.setId(user.getId());
        currentUser.setUsername(user.getUsername());
        currentUser.setFirstName(user.getFirstName());
        currentUser.setLastName(user.getLastName());
        currentUser.setEmailAddress(user.getEmailAddress());
        currentUser.setPhoneNumber(user.getPhoneNumber());
        currentUser = userRepository.save(user);
        return ResponseEntity.ok(currentUser);
    }

    @DeleteMapping("/{user_id}")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
