package org.svalero.memesconclase.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.svalero.memesconclase.domain.Comment;
import org.svalero.memesconclase.exception.CommentNotFoundException;
import org.svalero.memesconclase.repository.CommentRepository;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    public Comment get(long commentId) throws CommentNotFoundException {
        return commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
    }

    public Comment add(Comment comment) {
        return commentRepository.save(comment);
    }

    public void delete(long commentId) throws CommentNotFoundException {
        commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        commentRepository.deleteById(commentId);
    }
}
