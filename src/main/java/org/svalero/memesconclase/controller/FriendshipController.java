package org.svalero.memesconclase.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.svalero.memesconclase.domain.Friendship;
import org.svalero.memesconclase.domain.dto.FriendshipInDto;
import org.svalero.memesconclase.domain.dto.FriendshipOutDto;
import org.svalero.memesconclase.exception.FriendshipNotFoundException;
import org.svalero.memesconclase.exception.UserNotFoundException; // <-- Asegúrate de tener este import
import org.svalero.memesconclase.service.FriendshipService;

import java.util.List;

@RestController
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;
    private final Logger logger = LoggerFactory.getLogger(FriendshipController.class);

    @GetMapping("/friendships")
    public ResponseEntity<List<FriendshipOutDto>> getAll(
            @RequestParam(name = "userId", required = false) Long userId,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate)
    {
        logger.info("Begin Get all friendships");
        List<FriendshipOutDto> friendships = friendshipService.getAll(userId, status, startDate, endDate);
        logger.info("End Get all friendships");
        return new ResponseEntity<>(friendships, HttpStatus.OK);
    }

    @GetMapping("/friendships/{friendshipId}")
    public ResponseEntity<Friendship> getById(@PathVariable long friendshipId) throws FriendshipNotFoundException {
        logger.info("Begin Get friendship by id");
        Friendship friendship = friendshipService.get(friendshipId);
        logger.info("End Get friendship by id");
        return new ResponseEntity<>(friendship, HttpStatus.OK);
    }

    @PostMapping("/friendships")
    public ResponseEntity<FriendshipOutDto> addFriendship(@Valid @RequestBody FriendshipInDto friendshipInDto) throws UserNotFoundException {
        logger.info("Begin Add friendship");
        FriendshipOutDto addFriendship = friendshipService.add(friendshipInDto);
        logger.info("End Add friendship");
        return new ResponseEntity<>(addFriendship, HttpStatus.CREATED);
    }

    @PutMapping("/friendships/{friendshipId}")
    public ResponseEntity<FriendshipOutDto> modifyFriendship(@PathVariable long friendshipId, @Valid @RequestBody FriendshipInDto friendship) throws FriendshipNotFoundException{
        logger.info("Begin Modify friendship");
        FriendshipOutDto modifyFriendship = friendshipService.modify(friendshipId, friendship);
        logger.info("End Modify friendship");
        return new ResponseEntity<>(modifyFriendship, HttpStatus.OK);
    }

    @DeleteMapping("/friendships/{friendshipId}")
    public ResponseEntity<Void> delete(@PathVariable long friendshipId) throws FriendshipNotFoundException {
        logger.info("Begin Delete friendship");
        friendshipService.delete(friendshipId);
        logger.info("End Delete friendship");
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(FriendshipNotFoundException.class)
    public ResponseEntity<Void> handleNotFoundException(FriendshipNotFoundException e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Void> handleUserNotFoundException(UserNotFoundException unfe) {
        logger.error("Error al procesar amistad: " + unfe.getMessage());
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException iae) {
        logger.error("Error en la petición de amistad: " + iae.getMessage());
        return new ResponseEntity<>(iae.getMessage(), HttpStatus.BAD_REQUEST);
    }
}