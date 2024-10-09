package com.example.user_manager.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
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
import com.sanctionco.jmail.JMail;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository usersRepository) {
        this.userRepository = usersRepository;
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<Object> getUser(@PathVariable Long user_id) {
        Optional<User> user = userRepository.findById(user_id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody User user) throws URISyntaxException {
        try {
            User createdUser = userRepository.save(user);
            return ResponseEntity.created(new URI("/users/" + Long.toString(createdUser.getId()))).body(createdUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    @PutMapping("/{user_id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long user_id, @RequestBody User user) {
        if (userRepository.existsById(user_id)) {
            User currentUser = userRepository.findById(user_id).orElseThrow(RuntimeException::new);
            if (user.getEmailAddress() != null && JMail.isInvalid(user.getEmailAddress())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email address.");
            }
            currentUser.setUsername(user.getUsername());
            currentUser.setFirstName(user.getFirstName());
            currentUser.setLastName(user.getLastName());
            currentUser.setEmailAddress(user.getEmailAddress());
            currentUser.setPhoneNumber(user.getPhoneNumber());
            currentUser = userRepository.save(user);
            return ResponseEntity.ok(currentUser);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    @DeleteMapping("/{user_id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long user_id) {
        Optional<User> user = userRepository.findById(user_id);
        if (user.isPresent()) {
            userRepository.deleteById(user_id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }
}
