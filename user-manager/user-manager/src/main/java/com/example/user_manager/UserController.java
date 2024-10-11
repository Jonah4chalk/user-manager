package com.example.user_manager;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.IanaLinkRelations;

import com.sanctionco.jmail.JMail;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    private final UserModelAssembler assembler;

    public UserController(UserRepository usersRepository, UserModelAssembler assembler) {
        this.userRepository = usersRepository;
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<User>> getUsers() {
        List<EntityModel<User>> users = userRepository.findAll().stream().map(assembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(users, linkTo(methodOn(UserController.class).getUsers()).withSelfRel());
    }

    @GetMapping("/{user_id}")
    public EntityModel<User> getUser(@PathVariable Long user_id) {
        User user = userRepository.findById(user_id).orElseThrow(() -> new UserNotFoundException(user_id));
        return assembler.toModel(user);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody User user) throws URISyntaxException {
        try {
            EntityModel<User> userModel = assembler.toModel(userRepository.save(user));
            return ResponseEntity.created(userModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(userModel);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    @PutMapping("/{user_id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long user_id, @RequestBody User user) {
        User updatedUser = userRepository.findById(user_id).map(u -> {
            u.setUsername(user.getUsername());
            u.setName(user.getName());
            if (JMail.isValid(user.getEmailAddress())) {
                u.setEmailAddress(user.getEmailAddress());
            }
            u.setPhoneNumber(user.getPhoneNumber());
            return userRepository.save(u);
        }).orElseGet(() -> {
            return userRepository.save(user);
        });

        EntityModel<User> userModel = assembler.toModel(updatedUser);
        return ResponseEntity.created(userModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(userModel);
    }

    @DeleteMapping("/{user_id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long user_id) {
        Optional<User> user = userRepository.findById(user_id);
        if (user.isPresent()) {
            userRepository.deleteById(user_id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with ID " + user_id + " not found.");
        }
    }
}
