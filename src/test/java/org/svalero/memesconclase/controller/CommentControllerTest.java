package org.svalero.memesconclase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.svalero.memesconclase.domain.dto.CommentInDto;
import org.svalero.memesconclase.domain.dto.CommentOutDto;
import org.svalero.memesconclase.exception.CommentNotFoundException;
import org.svalero.memesconclase.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper om;
    @MockBean private CommentService commentService;

    private CommentInDto validIn() {
        CommentInDto in = new CommentInDto();
        in.setUserId(1L); in.setPublicationId(2L); in.setContent("nice");
        return in;
    }

    @Test void list_200() throws Exception {
        mvc.perform(get("/comments")).andExpect(status().isOk());
    }

    @Test void getById_404() throws Exception {
        Mockito.when(commentService.get(999L)).thenThrow(new CommentNotFoundException());
        mvc.perform(get("/comments/999")).andExpect(status().isNotFound());
    }

    @Test void add_201_y_400() throws Exception {
        CommentOutDto out = new CommentOutDto(); out.setId(7L);
        Mockito.when(commentService.add(any())).thenReturn(out);

        mvc.perform(post("/comments").contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(validIn()))).andExpect(status().isCreated());

        mvc.perform(post("/comments").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test void modify_200_400_404() throws Exception {
        CommentOutDto out = new CommentOutDto(); out.setId(5L);
        Mockito.when(commentService.modify(eq(5L), any())).thenReturn(out);
        mvc.perform(put("/comments/5").contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(validIn()))).andExpect(status().isOk());

        mvc.perform(put("/comments/5").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());

        Mockito.when(commentService.modify(eq(88L), any())).thenThrow(new CommentNotFoundException());
        mvc.perform(put("/comments/88").contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(validIn()))).andExpect(status().isNotFound());
    }

    @Test void delete_204_404() throws Exception {
        mvc.perform(delete("/comments/3")).andExpect(status().isNoContent());
        Mockito.verify(commentService).delete(3L);

        Mockito.doThrow(new CommentNotFoundException()).when(commentService).delete(44L);
        mvc.perform(delete("/comments/44")).andExpect(status().isNotFound());
    }
}

