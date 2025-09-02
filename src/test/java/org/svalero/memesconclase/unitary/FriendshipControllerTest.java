package org.svalero.memesconclase.unitary;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.svalero.memesconclase.controller.FriendshipController;
import org.svalero.memesconclase.domain.Friendship;
import org.svalero.memesconclase.domain.dto.FriendshipInDto;
import org.svalero.memesconclase.domain.dto.FriendshipOutDto;
import org.svalero.memesconclase.exception.FriendshipNotFoundException;
import org.svalero.memesconclase.service.FriendshipService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FriendshipController.class)
class FriendshipControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FriendshipService friendshipService;

    @Autowired
    private ObjectMapper objectMapper;

    private Friendship friendship;
    private FriendshipInDto friendshipInDto;
    private FriendshipOutDto friendshipOutDto;

    @BeforeEach
    void setUp() {
        friendship = new Friendship();
        friendship.setId(1L);
        friendship.setStatus(Friendship.Status.ACCEPTED);

        friendshipInDto = new FriendshipInDto();
        friendshipInDto.setUserId(1L);
        friendshipInDto.setFriendId(2L);
        friendshipInDto.setStatus("accepted");

        friendshipOutDto = new FriendshipOutDto();
        friendshipOutDto.setId(1L);
        friendshipOutDto.setStatus("accepted");
    }

    @Test
    void shouldGetAllFriendships() throws Exception {
        when(friendshipService.getAll("", null)).thenReturn(List.of(friendshipOutDto));

        mockMvc.perform(get("/friendships"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("accepted"));
    }

    @Test
    void shouldGetFriendshipById() throws Exception {
        when(friendshipService.get(1L)).thenReturn(friendship);

        mockMvc.perform(get("/friendships/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("accepted"));
    }

    @Test
    void shouldReturn404WhenFriendshipNotFound() throws Exception {
        when(friendshipService.get(999L)).thenThrow(new FriendshipNotFoundException());

        mockMvc.perform(get("/friendships/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateFriendship() throws Exception {
        when(friendshipService.add(any(FriendshipInDto.class))).thenReturn(friendshipOutDto);

        mockMvc.perform(post("/friendships")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(friendshipInDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("accepted"));
    }

    @Test
    void shouldUpdateFriendship() throws Exception {
        when(friendshipService.modify(eq(1L), any(FriendshipInDto.class))).thenReturn(friendshipOutDto);

        mockMvc.perform(put("/friendships/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(friendshipInDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("accepted"));
    }

    @Test
    void shouldDeleteFriendship() throws Exception {
        doNothing().when(friendshipService).delete(1L);

        mockMvc.perform(delete("/friendships/1"))
                .andExpect(status().isNoContent());
    }
}
