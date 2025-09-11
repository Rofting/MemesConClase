package org.svalero.memesconclase.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.svalero.memesconclase.domain.dto.PublicationInDto;
import org.svalero.memesconclase.domain.dto.PublicationOutDto;
import org.svalero.memesconclase.domain.dto.PublicationPatchDto;
import org.svalero.memesconclase.exception.PublicationNotFoundException;
import org.svalero.memesconclase.exception.UserNotFoundException;
import org.svalero.memesconclase.service.PublicationService;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

@RestController
public class PublicationController {

    @Autowired
    private PublicationService publicationService;
    private final Logger logger = LoggerFactory.getLogger(PublicationController.class);

    @GetMapping("/publications")
    public ResponseEntity<List<PublicationOutDto>> getAll(
            @RequestParam(name = "typeContent", required = false) String typeContent,
            @RequestParam(name = "privacy", required = false) String privacy,
            @RequestParam(name = "userId", required = false) Long userId) {
        logger.info("Begin getAll publications");
        List<PublicationOutDto> publications = publicationService.getAll(typeContent, privacy, userId);
        logger.info("End getAll publications");
        return new ResponseEntity<>(publications, HttpStatus.OK);
    }

    @GetMapping("/publications/{publicationId}")
    public ResponseEntity<PublicationOutDto> getPublicationById(@PathVariable long publicationId)
            throws PublicationNotFoundException {
        logger.info("Begin getPublicationById");
        PublicationOutDto publication = publicationService.get(publicationId);
        logger.info("End getPublicationById");
        return new ResponseEntity<>(publication, HttpStatus.OK);
    }

    @PutMapping("/publications/{publicationId}")
    public ResponseEntity<PublicationOutDto> modifyPublication(
            @PathVariable long publicationId,
            @Valid @RequestBody PublicationInDto publication) throws PublicationNotFoundException, UserNotFoundException {
        logger.info("Begin modifyPublication");
        PublicationOutDto modifyPublication = publicationService.modify(publicationId, publication);
        logger.info("End modifyPublication");
        return new ResponseEntity<>(modifyPublication, HttpStatus.OK);
    }

    @PostMapping("/publications")
    public ResponseEntity<PublicationOutDto> addPublication(@Valid @RequestBody PublicationInDto publicationInDto)
            throws UserNotFoundException {
        logger.info("Begin addPublication");
        PublicationOutDto addPublication = publicationService.add(publicationInDto);
        logger.info("End addPublication");
        return new ResponseEntity<>(addPublication, HttpStatus.CREATED);
    }

    @DeleteMapping("/publications/{publicationId}")
    public ResponseEntity<Void> deletePublication(@PathVariable long publicationId)
            throws PublicationNotFoundException {
        logger.info("Begin deletePublication");
        publicationService.delete(publicationId);
        logger.info("End deletePublication");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{userId}/publications")
    public ResponseEntity<List<PublicationOutDto>> getUserPublications(@PathVariable long userId) {
        logger.info("Begin getUserPublications by user {}", userId);
        List<PublicationOutDto> publications = publicationService.findByUser(userId);
        logger.info("End getUserPublications by user {}", userId);
        return new ResponseEntity<>(publications, HttpStatus.OK);
    }
    @PatchMapping("/publications/{publicationId}")
    public ResponseEntity<PublicationOutDto> patchPublication(
            @PathVariable long publicationId,
            @Valid @RequestBody PublicationPatchDto patch
    ) throws PublicationNotFoundException {
        PublicationOutDto result = publicationService.partialModify(publicationId, patch);
        return ResponseEntity.ok(result);
    }




    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        logger.error("Usuario no encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(PublicationNotFoundException.class)
    public ResponseEntity<String> handlePublicationNotFound(PublicationNotFoundException ex) {
        logger.error("Publication not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler({ IllegalArgumentException.class, DataIntegrityViolationException.class })
    public ResponseEntity<String> handleBadRequest(RuntimeException ex) {
        logger.error("Bad request: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidation(MethodArgumentNotValidException ex) {
        logger.error("Validation error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos inválidos: " + ex.getMessage());
    }
}
