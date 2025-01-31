package org.svalero.memesconclase.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger logger = LoggerFactory.getLogger(ReactionController.class);

    @GetMapping("/reactions")
    public ResponseEntity<List<ReactionOutDto>> getAll(@RequestParam(value = "type", defaultValue = "") String type,
                                                       @RequestParam(value = "publicationId", defaultValue = "") Long publicationId) {
        logger.info("Begin getAll reactions");
        List<ReactionOutDto> reactions = reactionService.getAll(type, publicationId);
        logger.info("End getAll reactions");
        return new ResponseEntity<>(reactions, HttpStatus.OK);
    }

    @GetMapping("/reactions/{reactionId}")
    public ResponseEntity<Reaction> getById(@PathVariable long reactionId) throws ReactionNotFoundException {
        logger.info("Begin getById reaction");
        Reaction reaction = reactionService.get(reactionId);
        logger.info("End getById reaction");
        return new ResponseEntity<>(reaction, HttpStatus.OK);
    }

    @PostMapping("/reactions")
    public ResponseEntity<ReactionOutDto> addReaction(@Valid @RequestBody ReactionInDto reactionInDto) throws ReactionNotFoundException {
        logger.info("Begin add reaction");
        ReactionOutDto addReaction = reactionService.add(reactionInDto);
        return new ResponseEntity<>(addReaction, HttpStatus.CREATED);
    }

    @PutMapping("/reactions/{reactionId}")
    public ResponseEntity<ReactionOutDto> modifyReaction(@PathVariable long reactionId, @Valid @RequestBody ReactionInDto reaction) throws ReactionNotFoundException {
        logger.info("Begin modify reaction");
        ReactionOutDto modifyReaction = reactionService.modify(reactionId,reaction);
        logger.info("End modify reaction");
        return new ResponseEntity<>(modifyReaction, HttpStatus.OK);
    }

    @DeleteMapping("/reactions/{reactionId}")
    public ResponseEntity<Void> delete(@PathVariable long reactionId) throws ReactionNotFoundException {
        logger.info("Begin delete reaction");
        reactionService.delete(reactionId);
        logger.info("End delete reaction");
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    public ResponseEntity<Void> handleNotFoundException(ReactionNotFoundException e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}







