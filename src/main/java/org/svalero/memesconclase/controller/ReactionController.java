package org.svalero.memesconclase.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.svalero.memesconclase.domain.dto.ReactionInDto;
import org.svalero.memesconclase.domain.dto.ReactionOutDto;
import org.svalero.memesconclase.domain.dto.ReactionPatchDto;
import org.svalero.memesconclase.exception.PublicationNotFoundException;
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
    public ResponseEntity<List<ReactionOutDto>> getAll(@RequestParam(value = "type", required = false) String type,
                                                       @RequestParam(value = "publicationId", required = false) Long publicationId,
                                                       @RequestParam(value = "userId", required = false) Long userId) {
        logger.info("Begin getAll reactions");
        List<ReactionOutDto> reactions = reactionService.getAllWithFilters(type, publicationId, userId);
        logger.info("End getAll reactions");
        return new ResponseEntity<>(reactions, HttpStatus.OK);
    }

    @GetMapping("/reactions/{reactionId}")
    public ResponseEntity<ReactionOutDto> getById(@PathVariable long reactionId) throws ReactionNotFoundException {
        logger.info("Begin getById reaction");
        ReactionOutDto reaction = reactionService.get(reactionId);
        logger.info("End getById reaction");
        return new ResponseEntity<>(reaction, HttpStatus.OK);
    }

    @PostMapping("/reactions")
    public ResponseEntity<ReactionOutDto> addReaction(@Valid @RequestBody ReactionInDto reactionInDto) throws UserNotFoundException, PublicationNotFoundException {
        logger.info("Begin add reaction");
        ReactionOutDto addReaction = reactionService.add(reactionInDto);
        logger.info("End add reaction");
        return new ResponseEntity<>(addReaction, HttpStatus.CREATED);
    }

    @PutMapping("/reactions/{reactionId}")
    public ResponseEntity<ReactionOutDto> modifyReaction(@PathVariable long reactionId, @Valid @RequestBody ReactionInDto reaction) throws ReactionNotFoundException {
        logger.info("Begin modify reaction");
        ReactionOutDto modifyReaction = reactionService.modify(reactionId,reaction);
        logger.info("End modify reaction");
        return new ResponseEntity<>(modifyReaction, HttpStatus.OK);
    }

    @PatchMapping("/reactions/{reactionId}")
    public ResponseEntity<ReactionOutDto> patchReaction(@PathVariable long reactionId, @Valid @RequestBody ReactionPatchDto patch) throws ReactionNotFoundException {
        logger.info("Begin patch reaction");
        ReactionOutDto result = reactionService.patch(reactionId, patch);
        logger.info("End patch reaction");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/reactions/{reactionId}")
    public ResponseEntity<Void> delete(@PathVariable long reactionId) throws ReactionNotFoundException {
        logger.info("Begin delete reaction");
        reactionService.delete(reactionId);
        logger.info("End delete reaction");
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ReactionNotFoundException.class)
    public ResponseEntity<Void> handleNotFoundException(ReactionNotFoundException e) {
        logger.error("Error: " + e.getMessage());
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Void> handleUserNotFoundException(UserNotFoundException unfe) {
        logger.error("Error al procesar reacción: " + unfe.getMessage());
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PublicationNotFoundException.class)
    public ResponseEntity<Void> handlePublicationNotFoundException(PublicationNotFoundException pnfe) {
        logger.error("Error al procesar reacción: " + pnfe.getMessage());
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}







