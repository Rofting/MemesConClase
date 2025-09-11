package org.svalero.memesconclase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.svalero.memesconclase.domain.dto.ReactionInDto;
import org.svalero.memesconclase.domain.dto.ReactionOutDto;
import org.svalero.memesconclase.exception.ReactionNotFoundException;
import org.svalero.memesconclase.service.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReactionController.class)
class ReactionControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper om;
    @MockBean private ReactionService reactionService;

    private ReactionInDto validIn() {
        ReactionInDto in = new ReactionInDto();
        in.setUserId(1L); in.setPublicationId(2L); in.setType("LIKE");
        return in;
    }

    @Test void list_200() throws Exception {
        mvc.perform(get("/reactions")).andExpect(status().isOk());
    }

    @Test void getById_404() throws Exception {
        Mockito.when(reactionService.get(999L)).thenThrow(new ReactionNotFoundException());
        mvc.perform(get("/reactions/999")).andExpect(status().isNotFound());
    }

    @Test void add_201_400() throws Exception {
        ReactionOutDto out = new ReactionOutDto(); out.setId(1L);
        Mockito.when(reactionService.add(any())).thenReturn(out);

        mvc.perform(post("/reactions").contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(validIn()))).andExpect(status().isCreated());

        mvc.perform(post("/reactions").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"publicationId\":2,\"type\":\"INVALID\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test void modify_200_400_404() throws Exception {
        ReactionOutDto out = new ReactionOutDto(); out.setId(5L);
        Mockito.when(reactionService.modify(eq(5L), any())).thenReturn(out);
        mvc.perform(put("/reactions/5").contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(validIn()))).andExpect(status().isOk());

        mvc.perform(put("/reactions/5").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"publicationId\":2,\"type\":\"INVALID\"}"))
                .andExpect(status().isBadRequest());

        Mockito.when(reactionService.modify(eq(77L), any())).thenThrow(new ReactionNotFoundException());
        mvc.perform(put("/reactions/77").contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(validIn()))).andExpect(status().isNotFound());
    }

    @Test void delete_204_404() throws Exception {
        mvc.perform(delete("/reactions/2")).andExpect(status().isNoContent());
        Mockito.doThrow(new ReactionNotFoundException()).when(reactionService).delete(88L);
        mvc.perform(delete("/reactions/88")).andExpect(status().isNotFound());
    }
}

