package org.svalero.memesconclase.service;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.svalero.memesconclase.domain.Comment;
import org.svalero.memesconclase.domain.dto.CommentInDto;
import org.svalero.memesconclase.domain.dto.CommentOutDto;
import org.svalero.memesconclase.exception.CommentNotFoundException;
import org.svalero.memesconclase.repository.CommentRepository;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<CommentOutDto> getAll(String content, Long publicationId) {
        List<Comment> commentList;

        if (content.isEmpty() && publicationId == null) {
            commentList = commentRepository.findAll();
        } else if (content.isEmpty()) {
            commentList = commentRepository.findByPublicationId(publicationId);
        } else if (publicationId == null) {
            commentList = commentRepository.findByContentContaining(content);
        } else {
            commentList = commentRepository.findByContentContainingAndPublicationId(content, publicationId);
        }

        return modelMapper.map(commentList, new TypeToken<List<CommentOutDto>>() {}.getType());
    }


    public Comment get(long commentId) throws CommentNotFoundException {
        return commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
    }

    public CommentOutDto add(CommentInDto commentInDto)  {
        Comment comment = modelMapper.map(commentInDto, Comment.class);
        comment = commentRepository.save(comment);
        return modelMapper.map(comment, CommentOutDto.class);
    }

    public void delete(long commentId) throws CommentNotFoundException {
        commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        commentRepository.deleteById(commentId);
    }

    public CommentOutDto modify(long commentId, CommentInDto commentInDto) throws CommentNotFoundException{
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        modelMapper.map(commentInDto, comment);
        commentRepository.save(comment);
        return modelMapper.map(comment, CommentOutDto.class);
    }
}
