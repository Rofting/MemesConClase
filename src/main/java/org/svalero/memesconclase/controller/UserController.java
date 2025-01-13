package org.svalero.memesconclase.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.svalero.memesconclase.domain.dto.UserInDto;
import org.svalero.memesconclase.domain.dto.UserOutDto;
import org.svalero.memesconclase.exception.UserNotFoundException;
import org.svalero.memesconclase.domain.User;
import org.svalero.memesconclase.service.UserService;

import java.util.List;

@RestController

public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserOutDto>> getAll(@RequestParam(value = "name",defaultValue = "") String name,
                                                   @RequestParam(value = "email", defaultValue = "") String email) {
        return new ResponseEntity<>(userService.getAll(name, email), HttpStatus.OK);
    }

    @GetMapping("/users/:userId")
    public ResponseEntity<User> getUserById(long userId) throws UserNotFoundException {
        User user = userService.get(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/users/:userId")
    public ResponseEntity<UserOutDto> modifyUser(long userId, @Valid @RequestBody UserInDto user) throws UserNotFoundException {
        UserOutDto modifyUser = userService.modify(userId,user);
        return new ResponseEntity<>(modifyUser, HttpStatus.OK);
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