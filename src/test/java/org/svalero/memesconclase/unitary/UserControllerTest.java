package org.svalero.memesconclase.unitary;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.svalero.memesconclase.controller.UserController;
import org.svalero.memesconclase.domain.User;
import org.svalero.memesconclase.domain.dto.UserInDto;
import org.svalero.memesconclase.domain.dto.UserOutDto;
import org.svalero.memesconclase.exception.UserNotFoundException;
import org.svalero.memesconclase.service.UserService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private UserInDto userInDto;
    private UserOutDto userOutDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Juan");
        user.setEmail("juan@mail.com");
        user.setPassword("1234");
        user.setBirthDate(LocalDate.of(2000, 1, 1));
        user.setAvatar("avatar.png");
        user.setPhone("123456789");
        user.setActive(true);
        user.setSex("Male");

        userInDto = new UserInDto();
        userInDto.setName("Juan");
        userInDto.setEmail("juan@mail.com");
        userInDto.setPassword("123456");
        userInDto.setBirthDate(LocalDate.of(2000, 1, 1));
        userInDto.setActive(true);
        userInDto.setSex("Male");
        userInDto.setAvatar("http://avatar.url");
        userInDto.setPhone("123456789");


        userOutDto = new UserOutDto(user);
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        when(userService.getAll("", "")).thenReturn(List.of(userOutDto));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Juan"));
    }

    @Test
    void shouldGetUserById() throws Exception {
        when(userService.get(1L)).thenReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Juan"));
    }

    @Test
    void shouldReturn404WhenUserNotFound() throws Exception {
        when(userService.get(999L)).thenThrow(new UserNotFoundException());

        mockMvc.perform(get("/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateUser() throws Exception {
        when(userService.add(any(UserInDto.class))).thenReturn(userOutDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Juan"));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        when(userService.modify(eq(1L), any(UserInDto.class))).thenReturn(userOutDto);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Juan"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        doNothing().when(userService).delete(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn400WhenInvalidUserData() throws Exception {
        UserInDto invalidDto = new UserInDto(); // campos vacíos, fallará validación

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }
}
