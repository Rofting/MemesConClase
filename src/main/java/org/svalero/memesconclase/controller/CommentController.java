package org.svalero.memesconclase.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.svalero.memesconclase.domain.Comment;
import org.svalero.memesconclase.domain.dto.CommentInDto;
import org.svalero.memesconclase.domain.dto.CommentOutDto;
import org.svalero.memesconclase.exception.CommentNotFoundException;
import org.svalero.memesconclase.service.CommentService;

import java.util.List;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;
    private final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @GetMapping("/comments")
    public ResponseEntity<List<CommentOutDto>> getAll(@RequestParam(value = "content", defaultValue = "") String content,
                                                      @RequestParam(value = "publicationId", defaultValue = "") Long publicationId) {
        logger.info("Begin getAll comments");
        List<CommentOutDto> comments = commentService.getAll(content, publicationId);
        logger.info("End getAll comments");
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<Comment> getById(@PathVariable long commentId) throws CommentNotFoundException {
        logger.info("Begin getById");
        Comment comment = commentService.get(commentId);
        logger.info("End getById");
        return new ResponseEntity<>(commentService.get(commentId), HttpStatus.OK);

    }

    @PostMapping("/comments")
    public ResponseEntity<CommentOutDto> addComment(@Valid @RequestBody CommentInDto commentInDto)throws CommentNotFoundException {
        logger.info("Begin add");
        CommentOutDto addComment = commentService.add(commentInDto);
        logger.info("End add");
        return new ResponseEntity<>(addComment, HttpStatus.CREATED);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentOutDto>modifyComment(@PathVariable  long commentId, @Valid @RequestBody CommentInDto comment) throws CommentNotFoundException {
        logger.info("Begin modify");
        CommentOutDto modifyComment = commentService.modify(commentId, comment);
        logger.info("End modify");
        return new ResponseEntity<>(modifyComment, HttpStatus.OK);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable long commentId) throws CommentNotFoundException {
        logger.info("Begin delete");
        commentService.delete(commentId);
        logger.info("End delete");
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    public ResponseEntity<Void> handleNotFoundException(CommentNotFoundException e) {
        logger.error(e.getMessage());
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
