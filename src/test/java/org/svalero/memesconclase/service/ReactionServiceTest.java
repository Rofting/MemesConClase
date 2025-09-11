package org.svalero.memesconclase.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.svalero.memesconclase.domain.Publication;
import org.svalero.memesconclase.domain.Reaction;
import org.svalero.memesconclase.domain.User;
import org.svalero.memesconclase.domain.dto.ReactionInDto;
import org.svalero.memesconclase.domain.dto.ReactionOutDto;
import org.svalero.memesconclase.exception.ReactionNotFoundException;
import org.svalero.memesconclase.repository.PublicationRepository;
import org.svalero.memesconclase.repository.ReactionRepository;
import org.svalero.memesconclase.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ReactionServiceTest {

    @Mock ReactionRepository reactionRepository;
    @Mock UserRepository userRepository;
    @Mock PublicationRepository publicationRepository;
    @Mock ModelMapper modelMapper;

    @InjectMocks ReactionService reactionService;

    ReactionInDto inDto;
    Reaction entity;
    ReactionOutDto outDto;

    @BeforeEach
    void setUp() {
        inDto = new ReactionInDto();
        inDto.setUserId(1L);
        inDto.setPublicationId(2L);
        inDto.setType("LIKE");

        entity = new Reaction();
        outDto = mock(ReactionOutDto.class);

        when(modelMapper.map(any(ReactionInDto.class), eq(Reaction.class))).thenReturn(entity);
        when(modelMapper.map(any(Reaction.class), eq(ReactionOutDto.class))).thenReturn(outDto);
        doAnswer(inv -> inv.getArgument(1))
                .when(modelMapper).map(any(ReactionInDto.class), any(Reaction.class));

        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(publicationRepository.findById(2L)).thenReturn(Optional.of(new Publication()));
    }

    @Test
    void add_ok() throws Exception {
        when(reactionRepository.save(entity)).thenReturn(entity);
        assertNotNull(reactionService.add(inDto));
        verify(reactionRepository).save(entity);
    }

    @Test
    void modify_delete_get_paths() throws Exception {
        Reaction existing = new Reaction();
        when(reactionRepository.findById(1L)).thenReturn(Optional.of(existing));
        assertNotNull(reactionService.get(1L));

        when(reactionRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(reactionRepository.save(existing)).thenReturn(existing);
        assertNotNull(reactionService.modify(1L, inDto));


        when(reactionRepository.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(reactionRepository).delete(existing);
        reactionService.delete(1L);
        verify(reactionRepository).delete(existing);

        when(reactionRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ReactionNotFoundException.class, () -> reactionService.get(99L));

        when(reactionRepository.findById(98L)).thenReturn(Optional.empty());
        assertThrows(ReactionNotFoundException.class, () -> reactionService.delete(98L));
    }
}
