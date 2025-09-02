package org.svalero.memesconclase.unitary;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.svalero.memesconclase.controller.CommentController;
import org.svalero.memesconclase.domain.Comment;
import org.svalero.memesconclase.domain.dto.CommentInDto;
import org.svalero.memesconclase.domain.dto.CommentOutDto;
import org.svalero.memesconclase.exception.CommentNotFoundException;
import org.svalero.memesconclase.service.CommentService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Comment comment;
    private CommentInDto commentInDto;
    private CommentOutDto commentOutDto;

    @BeforeEach
    void setUp() {
        comment = new Comment();
        comment.setId(1L);
        comment.setContent("Great post!");

        commentInDto = new CommentInDto();
        commentInDto.setContent("Great post!");
        commentInDto.setPublicationId(1L);

        commentOutDto = new CommentOutDto();
        commentOutDto.setId(1L);
        commentOutDto.setContent("Great post!");
    }

    @Test
    void shouldGetAllComments() throws Exception {
        when(commentService.getAll("", null)).thenReturn(List.of(commentOutDto));

        mockMvc.perform(get("/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("Great post!"));
    }

    @Test
    void shouldGetCommentById() throws Exception {
        when(commentService.get(1L)).thenReturn(comment);

        mockMvc.perform(get("/comments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Great post!"));
    }

    @Test
    void shouldReturn404WhenCommentNotFound() throws Exception {
        when(commentService.get(999L)).thenThrow(new CommentNotFoundException());

        mockMvc.perform(get("/comments/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateComment() throws Exception {
        when(commentService.add(any(CommentInDto.class))).thenReturn(commentOutDto);

        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentInDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("Great post!"));
    }

    @Test
    void shouldUpdateComment() throws Exception {
        when(commentService.modify(eq(1L), any(CommentInDto.class))).thenReturn(commentOutDto);

        mockMvc.perform(put("/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentInDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Great post!"));
    }

    @Test
    void shouldDeleteComment() throws Exception {
        doNothing().when(commentService).delete(1L);

        mockMvc.perform(delete("/comments/1"))
                .andExpect(status().isNoContent());
    }
}
