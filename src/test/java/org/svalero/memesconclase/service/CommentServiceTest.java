package org.svalero.memesconclase.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoSettings;
import org.svalero.memesconclase.domain.Comment;
import org.svalero.memesconclase.domain.Publication;
import org.svalero.memesconclase.domain.User;
import org.svalero.memesconclase.domain.dto.CommentInDto;
import org.svalero.memesconclase.domain.dto.CommentOutDto;
import org.svalero.memesconclase.exception.CommentNotFoundException;
import org.svalero.memesconclase.repository.CommentRepository;
import org.svalero.memesconclase.repository.PublicationRepository;
import org.svalero.memesconclase.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CommentServiceTest {

    @Mock CommentRepository commentRepository;
    @Mock UserRepository userRepository;
    @Mock PublicationRepository publicationRepository;
    @Mock ModelMapper modelMapper;

    @InjectMocks CommentService commentService;

    CommentInDto inDto;
    Comment entity;
    CommentOutDto outDto;

    @BeforeEach
    void setUp() {
        inDto = new CommentInDto();
        inDto.setUserId(1L);
        inDto.setPublicationId(2L);
        inDto.setContent("hola");

        entity = new Comment();
        outDto = mock(CommentOutDto.class);

        when(modelMapper.map(any(CommentInDto.class), eq(Comment.class))).thenReturn(entity);
        when(modelMapper.map(any(Comment.class), eq(CommentOutDto.class))).thenReturn(outDto);

        doAnswer(inv -> inv.getArgument(1))
                .when(modelMapper).map(any(CommentInDto.class), any(Comment.class));


        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(publicationRepository.findById(2L)).thenReturn(Optional.of(new Publication()));
    }

    @Test
    void add_ok() throws Exception {
        when(commentRepository.save(entity)).thenReturn(entity);

        var result = commentService.add(inDto);
        assertNotNull(result);
        verify(commentRepository).save(entity);
    }

    @Test
    void get_modify_delete_and_404() throws Exception {
        // GET OK
        when(commentRepository.findById(1L)).thenReturn(Optional.of(entity));
        assertNotNull(commentService.get(1L));

        // MODIFY OK
        Comment existing = new Comment();
        when(commentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(commentRepository.save(existing)).thenReturn(existing);
        assertNotNull(commentService.modify(1L, inDto));

        // DELETE OK
        when(commentRepository.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(commentRepository).delete(existing);
        commentService.delete(1L);
        verify(commentRepository).delete(existing);

        // GET 404
        when(commentRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(CommentNotFoundException.class, () -> commentService.get(99L));

        // DELETE 404
        when(commentRepository.findById(98L)).thenReturn(Optional.empty());
        assertThrows(CommentNotFoundException.class, () -> commentService.delete(98L));
    }
}
