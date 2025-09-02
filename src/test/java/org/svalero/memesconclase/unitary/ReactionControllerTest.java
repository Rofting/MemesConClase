package org.svalero.memesconclase.unitary;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.svalero.memesconclase.controller.ReactionController;
import org.svalero.memesconclase.domain.Reaction;
import org.svalero.memesconclase.domain.dto.ReactionInDto;
import org.svalero.memesconclase.domain.dto.ReactionOutDto;
import org.svalero.memesconclase.exception.ReactionNotFoundException;
import org.svalero.memesconclase.service.ReactionService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReactionController.class)
class ReactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReactionService reactionService;

    @Autowired
    private ObjectMapper objectMapper;

    private Reaction reaction;
    private ReactionInDto reactionInDto;
    private ReactionOutDto reactionOutDto;

    @BeforeEach
    void setUp() {
        reaction = new Reaction();
        reaction.setId(1L);
        reaction.setType(Reaction.ReactionType.LIKE);

        reactionInDto = new ReactionInDto();
        reactionInDto.setType("like");
        reactionInDto.setPublicationId(1L);
        reactionInDto.setUserId(1L);

        reactionOutDto = new ReactionOutDto();
        reactionOutDto.setId(1L);
        reactionOutDto.setType("like");
    }

    @Test
    void shouldGetAllReactions() throws Exception {
        when(reactionService.getAll("", null)).thenReturn(List.of(reactionOutDto));

        mockMvc.perform(get("/reactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("like"));
    }

    @Test
    void shouldGetReactionById() throws Exception {
        when(reactionService.get(1L)).thenReturn(reaction);

        mockMvc.perform(get("/reactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("like"));
    }

    @Test
    void shouldReturn404WhenReactionNotFound() throws Exception {
        when(reactionService.get(999L)).thenThrow(new ReactionNotFoundException());

        mockMvc.perform(get("/reactions/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateReaction() throws Exception {
        when(reactionService.add(any(ReactionInDto.class))).thenReturn(reactionOutDto);

        mockMvc.perform(post("/reactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reactionInDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value("like"));
    }

    @Test
    void shouldUpdateReaction() throws Exception {
        when(reactionService.modify(eq(1L), any(ReactionInDto.class))).thenReturn(reactionOutDto);

        mockMvc.perform(put("/reactions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reactionInDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("like"));
    }

    @Test
    void shouldDeleteReaction() throws Exception {
        doNothing().when(reactionService).delete(1L);

        mockMvc.perform(delete("/reactions/1"))
                .andExpect(status().isNoContent());
    }
}
