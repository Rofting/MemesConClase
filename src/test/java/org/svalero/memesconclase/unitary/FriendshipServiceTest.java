package org.svalero.memesconclase.unitary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.svalero.memesconclase.domain.Friendship;
import org.svalero.memesconclase.domain.User;
import org.svalero.memesconclase.domain.dto.FriendshipInDto;
import org.svalero.memesconclase.domain.dto.FriendshipOutDto;
import org.svalero.memesconclase.exception.FriendshipNotFoundException;
import org.svalero.memesconclase.repository.FriendshipRepository;
import org.svalero.memesconclase.service.FriendshipService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class FriendshipServiceTest {

    @Mock
    private FriendshipRepository friendshipRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private FriendshipService friendshipService;

    private Friendship friendship;
    private FriendshipInDto friendshipInDto;
    private FriendshipOutDto friendshipOutDto;
    private User user;
    private User friendUser;

    @BeforeEach
    void setUp() {
        openMocks(this);

        user = new User();
        user.setId(1L);
        user.setName("Juan");

        friendUser = new User();
        friendUser.setId(2L);
        friendUser.setName("Ana");

        friendship = new Friendship();
        friendship.setId(1L);
        friendship.setUser(user);
        friendship.setFriend(friendUser);
        friendship.setStatus(Friendship.Status.ACCEPTED);

        friendshipInDto = new FriendshipInDto();
        friendshipInDto.setUserId(1L);
        friendshipInDto.setFriendId(2L);
        friendshipInDto.setStatus("ACCEPTED");

        friendshipOutDto = new FriendshipOutDto();
        friendshipOutDto.setId(1L);
        friendshipOutDto.setStatus("ACCEPTED");
    }

    @Test
    void shouldGetFriendshipById() throws FriendshipNotFoundException {
        when(friendshipRepository.findById(1L)).thenReturn(Optional.of(friendship));

        Friendship found = friendshipService.get(1L);

        assertEquals(Friendship.Status.ACCEPTED, found.getStatus());
    }

    @Test
    void shouldThrowWhenFriendshipNotFound() {
        when(friendshipRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(FriendshipNotFoundException.class, () -> friendshipService.get(999L));
    }

    @Test
    void shouldAddFriendship() {
        when(modelMapper.map(friendshipInDto, Friendship.class)).thenReturn(friendship);
        when(friendshipRepository.save(friendship)).thenReturn(friendship);
        when(modelMapper.map(friendship, FriendshipOutDto.class)).thenReturn(friendshipOutDto);

        FriendshipOutDto result = friendshipService.add(friendshipInDto);

        assertEquals("ACCEPTED", result.getStatus());
    }

    @Test
    void shouldDeleteFriendship() throws FriendshipNotFoundException {
        when(friendshipRepository.findById(1L)).thenReturn(Optional.of(friendship));
        doNothing().when(friendshipRepository).deleteById(1L);

        assertDoesNotThrow(() -> friendshipService.delete(1L));
    }
}
