package org.svalero.memesconclase.unitary;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.svalero.memesconclase.controller.PublicationController;
import org.svalero.memesconclase.domain.dto.PublicationInDto;
import org.svalero.memesconclase.domain.dto.PublicationOutDto;
import org.svalero.memesconclase.exception.PublicationNotFoundException;
import org.svalero.memesconclase.service.PublicationService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PublicationController.class)
class PublicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PublicationService publicationService;

    @Autowired
    private ObjectMapper objectMapper;

    private PublicationInDto publicationInDto;
    private PublicationOutDto publicationOutDto;

    @BeforeEach
    void setUp() {
        publicationInDto = new PublicationInDto();
        publicationInDto.setTypeContent("image");
        publicationInDto.setPrivacy("public");
        publicationInDto.setUserId(1L);

        publicationOutDto = new PublicationOutDto();
        publicationOutDto.setId(1L);
        publicationOutDto.setTypeContent("image");
        publicationOutDto.setPrivacy("public");
    }

    @Test
    void shouldGetAllPublications() throws Exception {
        when(publicationService.getAll("", "")).thenReturn(List.of(publicationOutDto));

        mockMvc.perform(get("/publications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].typeContent").value("image"));
    }

    @Test
    void shouldGetPublicationById() throws Exception {
        when(publicationService.get(1L)).thenReturn(publicationOutDto);

        mockMvc.perform(get("/publications/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.typeContent").value("image"));
    }

    @Test
    void shouldReturn404WhenPublicationNotFound() throws Exception {
        when(publicationService.get(999L)).thenThrow(new PublicationNotFoundException());

        mockMvc.perform(get("/publications/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreatePublication() throws Exception {
        when(publicationService.add(any(PublicationInDto.class))).thenReturn(publicationOutDto);

        mockMvc.perform(post("/publications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(publicationInDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.typeContent").value("image"));
    }

    @Test
    void shouldUpdatePublication() throws Exception {
        when(publicationService.modify(eq(1L), any(PublicationInDto.class))).thenReturn(publicationOutDto);

        mockMvc.perform(put("/publications/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(publicationInDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.typeContent").value("image"));
    }

    @Test
    void shouldDeletePublication() throws Exception {
        doNothing().when(publicationService).delete(1L);

        mockMvc.perform(delete("/publications/1"))
                .andExpect(status().isNoContent());
    }
}
