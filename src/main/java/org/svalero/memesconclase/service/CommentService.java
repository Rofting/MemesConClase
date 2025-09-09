package org.svalero.memesconclase.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.svalero.memesconclase.domain.Comment;
import org.svalero.memesconclase.domain.Publication;
import org.svalero.memesconclase.domain.User;
import org.svalero.memesconclase.domain.dto.CommentInDto;
import org.svalero.memesconclase.domain.dto.CommentOutDto;
import org.svalero.memesconclase.exception.CommentNotFoundException;
import org.svalero.memesconclase.exception.PublicationNotFoundException;
import org.svalero.memesconclase.exception.UserNotFoundException;
import org.svalero.memesconclase.repository.CommentRepository;
import org.svalero.memesconclase.repository.PublicationRepository;
import org.svalero.memesconclase.repository.UserRepository;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PublicationRepository publicationRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<CommentOutDto> getAll(String content, Long publicationId) {
        List<Comment> commentList;
        if ((content == null || content.isEmpty()) && publicationId == null) {
            commentList = commentRepository.findAll();
        } else if (content == null || content.isEmpty()) {
            commentList = commentRepository.findByPublication_Id(publicationId);
        } else if (publicationId == null) {
            commentList = commentRepository.findByContentContaining(content);
        } else {
            commentList = commentRepository.findByContentContainingAndPublication_Id(content, publicationId);
        }
        return modelMapper.map(commentList, new TypeToken<List<CommentOutDto>>() {}.getType());
    }

    public CommentOutDto get(long commentId) throws CommentNotFoundException {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        return modelMapper.map(comment, CommentOutDto.class);
    }

    public CommentOutDto add(CommentInDto commentInDto) throws UserNotFoundException, PublicationNotFoundException {
        User user = userRepository.findById(commentInDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(commentInDto.getUserId())));
        Publication publication = publicationRepository.findById(commentInDto.getPublicationId())
                .orElseThrow(() -> new PublicationNotFoundException(String.valueOf(commentInDto.getPublicationId())));

        Comment newComment = modelMapper.map(commentInDto, Comment.class);
        newComment.setUser(user);
        newComment.setPublication(publication);

        Comment savedComment = commentRepository.save(newComment);
        return modelMapper.map(savedComment, CommentOutDto.class);
    }

    public void delete(long commentId) throws CommentNotFoundException {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        commentRepository.delete(comment);
    }

    public CommentOutDto modify(long commentId, CommentInDto commentInDto) throws CommentNotFoundException {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        modelMapper.map(commentInDto, comment);
        commentRepository.save(comment);
        return modelMapper.map(comment, CommentOutDto.class);
    }
}