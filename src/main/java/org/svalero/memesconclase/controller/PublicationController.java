package org.svalero.memesconclase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.svalero.memesconclase.exception.PublicationNotFoundException;
import org.svalero.memesconclase.model.Publication;
import org.svalero.memesconclase.service.PublicationService;

import java.util.List;

@RestController
public class PublicationController {

    @Autowired
    private PublicationService publicationService;

    @GetMapping("/publications")
    public ResponseEntity<List<Publication>> getAll() {
        return new ResponseEntity<>(publicationService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/publications/{publicationId}")
    public ResponseEntity<Publication> getPublicationById(@PathVariable long publicationId) throws PublicationNotFoundException {
        Publication publication = publicationService.get(publicationId);
        return new ResponseEntity<>(publication, HttpStatus.OK);
    }

    @PostMapping("/publications")
    public ResponseEntity<Publication> addPublication(@RequestBody Publication publication) {
        return new ResponseEntity<>(publicationService.add(publication), HttpStatus.CREATED);
    }

    @DeleteMapping("/publications/{publicationId}")
    public ResponseEntity<Void> deletePublication(@PathVariable long publicationId) throws PublicationNotFoundException {
        publicationService.delete(publicationId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    public ResponseEntity<Void> handlePublicationNotFoundException(PublicationNotFoundException e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
