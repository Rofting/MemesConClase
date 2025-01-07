package org.svalero.memesconclase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.svalero.memesconclase.exception.UserNotFoundException;
import org.svalero.memesconclase.model.User;
import org.svalero.memesconclase.service.UserService;

import java.util.List;

@RestController

public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAll() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/users/:userId")
    public ResponseEntity<User> getUserById(long userId) throws UserNotFoundException {
        User user = userService.get(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.add(user),HttpStatus.CREATED);
    }

    @DeleteMapping("/users/:userId")
    public ResponseEntity<Void> deleteUser(long userId) throws UserNotFoundException {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    public ResponseEntity<Void> handleUserNotFoundException(UserNotFoundException e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}