package org.svalero.memesconclase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.svalero.memesconclase.domain.dto.LoginRequest;
import org.svalero.memesconclase.domain.dto.UserInDto;
import org.svalero.memesconclase.domain.dto.UserOutDto;
import org.svalero.memesconclase.exception.UserNotFoundException;
import org.svalero.memesconclase.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper om;
    @MockBean private UserService userService;

    private UserInDto validIn() {
        UserInDto in = new UserInDto();
        in.setName("Alice"); in.setEmail("a@a.com"); in.setPassword("secret1");
        in.setBirthDate(LocalDate.of(1990,1,1)); in.setActive(true); in.setSex("Female");
        return in;
    }

    @Test void list_200() throws Exception {
        Mockito.when(userService.getAll(null)).thenReturn(Collections.emptyList());
        mvc.perform(get("/users")).andExpect(status().isOk());
    }

    @Test void getById_200() throws Exception {
        UserOutDto out = new UserOutDto(); out.setId(1L); out.setName("Alice");
        Mockito.when(userService.get(1L)).thenReturn(out);
        mvc.perform(get("/users/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1));
    }

    @Test void getById_404() throws Exception {
        Mockito.when(userService.get(999L)).thenThrow(new UserNotFoundException());
        mvc.perform(get("/users/999")).andExpect(status().isNotFound());
    }

    @Test void add_201() throws Exception {
        UserOutDto out = new UserOutDto(); out.setId(10L);
        Mockito.when(userService.add(any())).thenReturn(out);
        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(validIn())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test void add_400() throws Exception {
        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test void modify_200() throws Exception {
        UserOutDto out = new UserOutDto(); out.setId(1L);
        Mockito.when(userService.modify(eq(1L), any())).thenReturn(out);
        mvc.perform(put("/users/1").contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(validIn())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test void modify_400() throws Exception {
        mvc.perform(put("/users/1").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test void modify_404() throws Exception {
        Mockito.when(userService.modify(eq(99L), any())).thenThrow(new UserNotFoundException());
        mvc.perform(put("/users/99").contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(validIn())))
                .andExpect(status().isNotFound());
    }

    @Test void delete_204() throws Exception {
        mvc.perform(delete("/users/1")).andExpect(status().isNoContent());
        Mockito.verify(userService).delete(1L);
    }

    @Test void delete_404() throws Exception {
        Mockito.doThrow(new UserNotFoundException()).when(userService).delete(77L);
        mvc.perform(delete("/users/77")).andExpect(status().isNotFound());
    }

    @Test void login_200_y_401() throws Exception {
        UserOutDto out = new UserOutDto(); out.setId(1L); out.setEmail("a@a.com");
        Mockito.when(userService.login("a@a.com","secret1")).thenReturn(out);
        Mockito.when(userService.login("a@a.com","bad")).thenReturn(null);

        LoginRequest ok = new LoginRequest("a@a.com","secret1");
        mvc.perform(post("/users/login").contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(ok)))
                .andExpect(status().isOk());

        LoginRequest fail = new LoginRequest("a@a.com","bad");
        mvc.perform(post("/users/login").contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(fail)))
                .andExpect(status().isUnauthorized());
    }
}
