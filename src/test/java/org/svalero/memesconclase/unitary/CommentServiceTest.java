package org.svalero.memesconclase.unitary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.svalero.memesconclase.domain.Comment;
import org.svalero.memesconclase.domain.dto.CommentInDto;
import org.svalero.memesconclase.domain.dto.CommentOutDto;
import org.svalero.memesconclase.exception.CommentNotFoundException;
import org.svalero.memesconclase.repository.CommentRepository;
import org.svalero.memesconclase.service.CommentService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CommentService commentService;

    private Comment comment;
    private CommentInDto commentInDto;
    private CommentOutDto commentOutDto;

    @BeforeEach
    void setUp() {
        openMocks(this);

        comment = new Comment();
        comment.setId(1L);
        comment.setContent("Nice post!");

        commentInDto = new CommentInDto();
        commentInDto.setContent("Nice post!");

        commentOutDto = new CommentOutDto();
        commentOutDto.setId(1L);
        commentOutDto.setContent("Nice post!");
    }

    @Test
    void shouldGetCommentById() throws CommentNotFoundException {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        Comment found = commentService.get(1L);

        assertEquals("Nice post!", found.getContent());
    }

    @Test
    void shouldThrowWhenCommentNotFound() {
        when(commentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () -> commentService.get(999L));
    }

    @Test
    void shouldAddComment() {
        when(modelMapper.map(commentInDto, Comment.class)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(comment);
        when(modelMapper.map(comment, CommentOutDto.class)).thenReturn(commentOutDto);

        CommentOutDto result = commentService.add(commentInDto);

        assertEquals("Nice post!", result.getContent());
    }

    @Test
    void shouldDeleteComment() throws CommentNotFoundException {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        doNothing().when(commentRepository).deleteById(1L);

        assertDoesNotThrow(() -> commentService.delete(1L));
    }
}
