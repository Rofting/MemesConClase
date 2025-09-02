package org.svalero.memesconclase.unitary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.svalero.memesconclase.domain.Reaction;
import org.svalero.memesconclase.domain.dto.ReactionInDto;
import org.svalero.memesconclase.domain.dto.ReactionOutDto;
import org.svalero.memesconclase.exception.ReactionNotFoundException;
import org.svalero.memesconclase.repository.ReactionRepository;
import org.svalero.memesconclase.service.ReactionService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class ReactionServiceTest {

    @Mock
    private ReactionRepository reactionRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ReactionService reactionService;

    private Reaction reaction;
    private ReactionInDto reactionInDto;
    private ReactionOutDto reactionOutDto;

    @BeforeEach
    void setUp() {
        openMocks(this);

        reaction = new Reaction();
        reaction.setId(1L);
        reaction.setType(Reaction.ReactionType.LIKE);

        reactionInDto = new ReactionInDto();
        reactionInDto.setType("LIKE");
        reactionInDto.setPublicationId(1L);
        reactionInDto.setUserId(1L);

        reactionOutDto = new ReactionOutDto();
        reactionOutDto.setId(1L);
        reactionOutDto.setType("LIKE");
    }

    @Test
    void shouldGetReactionById() throws ReactionNotFoundException {
        when(reactionRepository.findById(1L)).thenReturn(Optional.of(reaction));

        Reaction found = reactionService.get(1L);

        assertEquals(Reaction.ReactionType.LIKE, found.getType());
    }

    @Test
    void shouldThrowWhenReactionNotFound() {
        when(reactionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ReactionNotFoundException.class, () -> reactionService.get(999L));
    }

    @Test
    void shouldAddReaction() {
        when(modelMapper.map(reactionInDto, Reaction.class)).thenReturn(reaction);
        when(reactionRepository.save(reaction)).thenReturn(reaction);
        when(modelMapper.map(reaction, ReactionOutDto.class)).thenReturn(reactionOutDto);

        ReactionOutDto result = reactionService.add(reactionInDto);

        assertEquals("LIKE", result.getType());
    }

    @Test
    void shouldDeleteReaction() throws ReactionNotFoundException {
        when(reactionRepository.findById(1L)).thenReturn(Optional.of(reaction));
        doNothing().when(reactionRepository).deleteById(1L);

        assertDoesNotThrow(() -> reactionService.delete(1L));
    }
}
