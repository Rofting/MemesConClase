package org.svalero.memesconclase.controller;

import jakarta.validation.Valid;
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

    @GetMapping("/friendships")
    public ResponseEntity<List<FriendshipOutDto>> getAll(@RequestParam(value = "status", defaultValue = "") String status,
                                                         @RequestParam(value = "userId", defaultValue = "") Long userId) {
        return new ResponseEntity<>(friendshipService.getAll(status, userId), HttpStatus.OK);
    }

    @GetMapping("/friendships/:friendshipId")
    public ResponseEntity<Friendship> getById(@PathVariable long friendshipId) throws FriendshipNotFoundException {
        return new ResponseEntity<>(friendshipService.get(friendshipId), HttpStatus.OK);
    }

    @PostMapping("/friendships")
    public ResponseEntity<Friendship> add(@RequestBody Friendship friendship) {
        return new ResponseEntity<>(friendshipService.add(friendship), HttpStatus.CREATED);
    }

    @PutMapping("/friendships/:friendshipId")
    public ResponseEntity<FriendshipOutDto>modifyFriendship(long friendshipId,@Valid @RequestBody FriendshipInDto friendship) throws FriendshipNotFoundException{
        FriendshipOutDto modifyFriendship = friendshipService.modify(friendshipId, friendship);
        return new ResponseEntity<>(modifyFriendship, HttpStatus.OK);
    }

    @DeleteMapping("/friendships/:friendshipId")
    public ResponseEntity<Void> delete(@PathVariable long friendshipId) throws FriendshipNotFoundException {
        friendshipService.delete(friendshipId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    public ResponseEntity<Void> handleNotFoundException(FriendshipNotFoundException e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
