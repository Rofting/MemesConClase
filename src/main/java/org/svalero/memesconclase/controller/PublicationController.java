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
import org.svalero.memesconclase.exception.PublicationNotFoundException;
import org.svalero.memesconclase.service.PublicationService;

import java.util.List;

@RestController
public class PublicationController {

    @Autowired
    private PublicationService publicationService;
    private final Logger logger = LoggerFactory.getLogger(PublicationController.class);

    @GetMapping("/publications")
    public ResponseEntity<List<PublicationOutDto>> getAll(
            @RequestParam(value = "typeContent", defaultValue = "") String typeContent,
            @RequestParam(value = "privacy", defaultValue = "") String privacy) {
        logger.info("Begin getAll");
        List<PublicationOutDto> publications = publicationService.getAll(typeContent, privacy);
        logger.info("End getAll");
        return new ResponseEntity<>(publications, HttpStatus.OK);
    }

    @GetMapping("/publications/{publicationId}")
    public ResponseEntity<PublicationOutDto> getPublicationById(@PathVariable long publicationId) throws PublicationNotFoundException {
        logger.info("Begin getPublicationById");
        PublicationOutDto publication = publicationService.get(publicationId); // Ahora devuelve PublicationOutDto
        logger.info("End getPublicationById");
        return new ResponseEntity<>(publication, HttpStatus.OK);
    }

    @PutMapping("/publications/{publicationId}")
    public ResponseEntity<PublicationOutDto> modifyPublication(
            @PathVariable long publicationId,
            @Valid @RequestBody PublicationInDto publication) throws PublicationNotFoundException {
        logger.info("Begin modifyPublication");
        PublicationOutDto modifyPublication = publicationService.modify(publicationId, publication);
        logger.info("End modifyPublication");
        return new ResponseEntity<>(modifyPublication, HttpStatus.OK);
    }

    @PostMapping("/publications")
    public ResponseEntity<PublicationOutDto> addPublication(@Valid @RequestBody PublicationInDto publicationInDto) {
        logger.info("Begin addPublication");
        PublicationOutDto addPublication = publicationService.add(publicationInDto); // Devuelve PublicationOutDto
        logger.info("End addPublication");
        return new ResponseEntity<>(addPublication, HttpStatus.CREATED);
    }

    @DeleteMapping("/publications/{publicationId}")
    public ResponseEntity<Void> deletePublication(@PathVariable long publicationId) throws PublicationNotFoundException {
        logger.info("Begin deletePublication");
        publicationService.delete(publicationId);
        logger.info("End deletePublication");
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    public ResponseEntity<Void> handlePublicationNotFoundException(PublicationNotFoundException e) {
        logger.error("Publication not found: " + e.getMessage());
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
