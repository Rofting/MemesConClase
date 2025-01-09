package org.svalero.memesconclase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.svalero.memesconclase.domain.Reaction;
import org.svalero.memesconclase.exception.ReactionNotFoundException;
import org.svalero.memesconclase.service.ReactionService;

import java.util.List;

@RestController
public class ReactionController {

    @Autowired
    private ReactionService reactionService;

    @GetMapping("/reactions")
    public ResponseEntity<List<Reaction>> getAll() {
        return new ResponseEntity<>(reactionService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/reactions/{reactionId}")
    public ResponseEntity<Reaction> getById(@PathVariable long reactionId) throws ReactionNotFoundException {
        return new ResponseEntity<>(reactionService.get(reactionId), HttpStatus.OK);
    }

    @PostMapping("/reactions")
    public ResponseEntity<Reaction> add(@RequestBody Reaction reaction) {
        return new ResponseEntity<>(reactionService.add(reaction), HttpStatus.CREATED);
    }

    @DeleteMapping("/reactions/{reactionId}")
    public ResponseEntity<Void> delete(@PathVariable long reactionId) throws ReactionNotFoundException {
        reactionService.delete(reactionId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    public ResponseEntity<Void> handleNotFoundException(ReactionNotFoundException e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
