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
import org.svalero.memesconclase.service.FriendshipService;

import java.util.List;

@RestController
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;
    private final Logger logger = LoggerFactory.getLogger(FriendshipController.class);

    @GetMapping("/friendships")
    public ResponseEntity<List<FriendshipOutDto>> getAll(@RequestParam(value = "status", defaultValue = "") String status,
                                                         @RequestParam(value = "userId", defaultValue = "") Long userId) {
        logger.info("Begin Get all friendships");
        List<FriendshipOutDto> friendships = friendshipService.getAll(status, userId);
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
    public ResponseEntity<FriendshipOutDto> addFriendship(@Valid @RequestBody FriendshipInDto friendshipInDto) {
        logger.info("Begin Add friendship");
        FriendshipOutDto addFriendship = friendshipService.add(friendshipInDto);
        logger.info("End Add friendship");
        return new ResponseEntity<>(addFriendship, HttpStatus.CREATED);
    }

    @PutMapping("/friendships/{friendshipId}")
    public ResponseEntity<FriendshipOutDto>modifyFriendship(@PathVariable long friendshipId, @Valid @RequestBody FriendshipInDto friendship) throws FriendshipNotFoundException{
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

    @ExceptionHandler
    public ResponseEntity<Void> handleNotFoundException(FriendshipNotFoundException e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
