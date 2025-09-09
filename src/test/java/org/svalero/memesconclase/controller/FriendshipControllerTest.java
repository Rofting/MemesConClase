package org.svalero.memesconclase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.svalero.memesconclase.domain.dto.FriendshipInDto;
import org.svalero.memesconclase.domain.dto.FriendshipOutDto;
import org.svalero.memesconclase.exception.FriendshipNotFoundException;
import org.svalero.memesconclase.service.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FriendshipController.class)
class FriendshipControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper om;
    @MockBean private FriendshipService friendshipService;

    private FriendshipInDto validIn() {
        FriendshipInDto in = new FriendshipInDto();
        in.setUserId(1L); in.setFriendId(2L); in.setStatus("PENDING");
        return in;
    }

    @Test void list_200() throws Exception {
        mvc.perform(get("/friendships")).andExpect(status().isOk());
    }

    @Test void getById_404() throws Exception {
        Mockito.when(friendshipService.get(999L)).thenThrow(new FriendshipNotFoundException());
        mvc.perform(get("/friendships/999")).andExpect(status().isNotFound());
    }

    @Test void add_201_400() throws Exception {
        FriendshipOutDto out = new FriendshipOutDto(); out.setId(1L);
        Mockito.when(friendshipService.add(any())).thenReturn(out);

        mvc.perform(post("/friendships").contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(validIn()))).andExpect(status().isCreated());

        mvc.perform(post("/friendships").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"friendId\":2,\"status\":\"BANANA\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test void modify_200_400_404() throws Exception {
        FriendshipOutDto out = new FriendshipOutDto(); out.setId(3L);
        Mockito.when(friendshipService.modify(eq(3L), any())).thenReturn(out);
        mvc.perform(put("/friendships/3").contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(validIn()))).andExpect(status().isOk());

        mvc.perform(put("/friendships/3").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"friendId\":2,\"status\":\"BANANA\"}"))
                .andExpect(status().isBadRequest());

        Mockito.when(friendshipService.modify(eq(77L), any())).thenThrow(new FriendshipNotFoundException());
        mvc.perform(put("/friendships/77").contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(validIn()))).andExpect(status().isNotFound());
    }

    @Test void delete_204_404() throws Exception {
        mvc.perform(delete("/friendships/2")).andExpect(status().isNoContent());
        Mockito.doThrow(new FriendshipNotFoundException()).when(friendshipService).delete(88L);
        mvc.perform(delete("/friendships/88")).andExpect(status().isNotFound());
    }
}
