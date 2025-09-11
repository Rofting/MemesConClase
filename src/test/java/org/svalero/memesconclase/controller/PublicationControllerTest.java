package org.svalero.memesconclase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.svalero.memesconclase.domain.dto.PublicationInDto;
import org.svalero.memesconclase.domain.dto.PublicationOutDto;
import org.svalero.memesconclase.exception.PublicationNotFoundException;
import org.svalero.memesconclase.service.PublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PublicationController.class)
class PublicationControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper om;
    @MockBean private PublicationService publicationService;

    private PublicationInDto validIn() {
        PublicationInDto in = new PublicationInDto();
        in.setUserId(1L);
        in.setContent("hola");
        in.setTypeContent("TEXT");
        in.setPrivacy("PUBLIC");
        in.setLatitude(10.0);
        in.setLongitude(20.0);
        return in;
    }

    @Test
    void list_200() throws Exception {
        // 👇 nueva firma con TRES parámetros
        Mockito.when(publicationService.getAll(null, null, null)).thenReturn(Collections.emptyList());
        mvc.perform(get("/publications"))
                .andExpect(status().isOk());
    }

    @Test
    void list_200_with_filters() throws Exception {
        Mockito.when(publicationService.getAll("TEXT", "PUBLIC", 1L)).thenReturn(Collections.emptyList());
        mvc.perform(get("/publications")
                        .param("typeContent", "TEXT")
                        .param("privacy", "PUBLIC")
                        .param("userId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void getById_200() throws Exception {
        PublicationOutDto out = new PublicationOutDto(); out.setId(3L); out.setContent("x");
        Mockito.when(publicationService.get(3L)).thenReturn(out);
        mvc.perform(get("/publications/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3));
    }

    @Test
    void getById_404() throws Exception {
        Mockito.when(publicationService.get(999L)).thenThrow(new PublicationNotFoundException());
        mvc.perform(get("/publications/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void add_201() throws Exception {
        PublicationOutDto out = new PublicationOutDto(); out.setId(10L);
        Mockito.when(publicationService.add(any())).thenReturn(out);
        mvc.perform(post("/publications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(validIn())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void add_400() throws Exception {
        mvc.perform(post("/publications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void modify_200() throws Exception {
        PublicationOutDto out = new PublicationOutDto(); out.setId(8L);
        Mockito.when(publicationService.modify(eq(8L), any())).thenReturn(out);
        mvc.perform(put("/publications/8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(validIn())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(8));
    }

    @Test
    void modify_400() throws Exception {
        mvc.perform(put("/publications/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void modify_404() throws Exception {
        Mockito.when(publicationService.modify(eq(77L), any())).thenThrow(new PublicationNotFoundException());
        mvc.perform(put("/publications/77")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(validIn())))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_204_y_404() throws Exception {
        mvc.perform(delete("/publications/5"))
                .andExpect(status().isNoContent());
        Mockito.verify(publicationService).delete(5L);

        Mockito.doThrow(new PublicationNotFoundException()).when(publicationService).delete(66L);
        mvc.perform(delete("/publications/66"))
                .andExpect(status().isNotFound());
    }
}
