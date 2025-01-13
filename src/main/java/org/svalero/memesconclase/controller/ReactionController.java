package org.svalero.memesconclase.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.svalero.memesconclase.domain.Reaction;
import org.svalero.memesconclase.domain.dto.ReactionInDto;
import org.svalero.memesconclase.domain.dto.ReactionOutDto;
import org.svalero.memesconclase.domain.dto.UserInDto;
import org.svalero.memesconclase.domain.dto.UserOutDto;
import org.svalero.memesconclase.exception.ReactionNotFoundException;
import org.svalero.memesconclase.exception.UserNotFoundException;
import org.svalero.memesconclase.service.ReactionService;

import java.util.List;

@RestController
public class ReactionController {

    @Autowired
    private ReactionService reactionService;

    @GetMapping("/reactions")
    public ResponseEntity<List<ReactionOutDto>> getAll(@RequestParam(value = "type", defaultValue = "") String type,
                                                       @RequestParam(value = "publicationId", defaultValue = "") Long publicationId) {
        return new ResponseEntity<>(reactionService.getAll(type, publicationId), HttpStatus.OK);
    }

    @GetMapping("/reactions/:reactionId")
    public ResponseEntity<Reaction> getById(@PathVariable long reactionId) throws ReactionNotFoundException {
        return new ResponseEntity<>(reactionService.get(reactionId), HttpStatus.OK);
    }

    @PostMapping("/reactions")
    public ResponseEntity<Reaction> add(@RequestBody Reaction reaction) {
        return new ResponseEntity<>(reactionService.add(reaction), HttpStatus.CREATED);
    }

    @PutMapping("/reactions/:reactionId")
    public ResponseEntity<ReactionOutDto> modifyReaction(long reactionId, @Valid @RequestBody ReactionInDto reaction) throws ReactionNotFoundException {
        ReactionOutDto modifyReaction = reactionService.modify(reactionId,reaction);
        return new ResponseEntity<>(modifyReaction, HttpStatus.OK);
    }

    @DeleteMapping("/reactions/:reactionId")
    public ResponseEntity<Void> delete(@PathVariable long reactionId) throws ReactionNotFoundException {
        reactionService.delete(reactionId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    public ResponseEntity<Void> handleNotFoundException(ReactionNotFoundException e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
