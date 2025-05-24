package org.svalero.memesconclase.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.svalero.memesconclase.domain.dto.UserInDto;
import org.svalero.memesconclase.domain.dto.UserOutDto;
import org.svalero.memesconclase.exception.UserNotFoundException;
import org.svalero.memesconclase.domain.User;
import org.svalero.memesconclase.service.UserService;
import org.svalero.memesconclase.domain.dto.LoginRequest;


import java.util.List;


@RestController

public class UserController {
    @Autowired
    private UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/users")
    public ResponseEntity<List<UserOutDto>> getAll(@RequestParam(value = "name",defaultValue = "") String name,
                                                   @RequestParam(value = "email", defaultValue = "") String email) {
        logger.info("BEGIN getAll");
        List<UserOutDto> users = userService.getAll(name, email);
        logger.info("END getAll");
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable long userId) throws UserNotFoundException {
        logger.info("BEGIN getUser");
        User user = userService.get(userId);
        logger.info("END getUser");
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<UserOutDto> modifyUser(@PathVariable long userId, @Valid @RequestBody UserInDto user) throws UserNotFoundException {
        logger.info("BEGIN modifyUser");
        UserOutDto modifyUser = userService.modify(userId,user);
        logger.info("END modifyUser");
        return new ResponseEntity<>(modifyUser, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<UserOutDto> addUser(@Valid @RequestBody UserInDto userInDto) throws UserNotFoundException {
        logger.info("BEGIN addUser");
        UserOutDto addUser = userService.add(userInDto);
        logger.info("END addUser");
        return new ResponseEntity<>(addUser, HttpStatus.CREATED);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable long userId) throws UserNotFoundException {
        logger.info("BEGIN deleteUser");
        userService.delete(userId);
        logger.info("END deleteUser");
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/users/login")
    public ResponseEntity<UserOutDto> login(@RequestBody LoginRequest loginRequest) {
        logger.info("BEGIN login");

        // Llamamos al servicio para validar el usuario
        UserOutDto user = userService.login(loginRequest.getEmail(), loginRequest.getPassword());

        if (user != null) {
            logger.info("END login - success");
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            logger.warn("END login - unauthorized");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


    @ExceptionHandler
    public ResponseEntity<Void> handleUserNotFoundException(UserNotFoundException exception) {
        logger.error(exception.getMessage(),exception);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}