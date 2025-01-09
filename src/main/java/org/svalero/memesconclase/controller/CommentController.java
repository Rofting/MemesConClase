package org.svalero.memesconclase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.svalero.memesconclase.domain.Comment;
import org.svalero.memesconclase.exception.CommentNotFoundException;
import org.svalero.memesconclase.service.CommentService;

import java.util.List;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/comments")
    public ResponseEntity<List<Comment>> getAll() {
        return new ResponseEntity<>(commentService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<Comment> getById(@PathVariable long commentId) throws CommentNotFoundException {
        return new ResponseEntity<>(commentService.get(commentId), HttpStatus.OK);
    }

    @PostMapping("/comments")
    public ResponseEntity<Comment> add(@RequestBody Comment comment) {
        return new ResponseEntity<>(commentService.add(comment), HttpStatus.CREATED);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable long commentId) throws CommentNotFoundException {
        commentService.delete(commentId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    public ResponseEntity<Void> handleNotFoundException(CommentNotFoundException e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
