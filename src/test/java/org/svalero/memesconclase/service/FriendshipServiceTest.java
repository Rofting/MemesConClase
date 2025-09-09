package org.svalero.memesconclase.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.svalero.memesconclase.domain.Friendship;
import org.svalero.memesconclase.domain.User;
import org.svalero.memesconclase.domain.dto.FriendshipInDto;
import org.svalero.memesconclase.domain.dto.FriendshipOutDto;
import org.svalero.memesconclase.domain.dto.UserOutDto;
import org.svalero.memesconclase.repository.FriendshipRepository;
import org.svalero.memesconclase.repository.UserRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FriendshipServiceTest {

    @Mock private FriendshipRepository friendshipRepository;
    @Mock private UserRepository userRepository;

    @Mock private ModelMapper modelMapper;

    @Mock private TypeMap<FriendshipInDto, Friendship> friendshipInTypeMap;
    @Mock private TypeMap<User, UserOutDto> userToUserOutTypeMap;
    @Mock private TypeMap<Friendship, FriendshipOutDto> friendshipOutTypeMap;

    @InjectMocks
    private FriendshipService friendshipService;

    private void stubCommonModelMapper() {

        lenient().when(modelMapper.typeMap(FriendshipInDto.class, Friendship.class))
                .thenReturn(friendshipInTypeMap);
        lenient().when(friendshipInTypeMap.map(any(FriendshipInDto.class)))
                .thenReturn(new Friendship());

        lenient().when(modelMapper.typeMap(User.class, UserOutDto.class))
                .thenReturn(userToUserOutTypeMap);
        lenient().when(userToUserOutTypeMap.map(any(User.class)))
                .thenReturn(new UserOutDto());

        lenient().when(modelMapper.map(any(Friendship.class), eq(FriendshipOutDto.class)))
                .thenReturn(new FriendshipOutDto());
        lenient().when(modelMapper.typeMap(Friendship.class, FriendshipOutDto.class))
                .thenReturn(friendshipOutTypeMap);
        lenient().when(friendshipOutTypeMap.map(any(Friendship.class)))
                .thenReturn(new FriendshipOutDto());
    }

    @Test
    void add_ok() throws Exception {

        FriendshipInDto dto = new FriendshipInDto();
        dto.setUserId(1L);
        dto.setFriendId(2L);
        dto.setStatus("PENDING");

        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(userRepository.findById(2L)).thenReturn(Optional.of(new User()));
        when(friendshipRepository.save(any(Friendship.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        stubCommonModelMapper();

        friendshipService.add(dto);

        verify(friendshipRepository, times(1)).save(any(Friendship.class));
    }

    @Test
    void get_modify_delete_paths() throws Exception {

        Friendship existing = new Friendship();
        when(friendshipRepository.findById(1L)).thenReturn(Optional.of(existing));

        FriendshipInDto dto = new FriendshipInDto();
        dto.setUserId(1L);
        dto.setFriendId(2L);
        dto.setStatus("ACCEPTED");

        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(userRepository.findById(2L)).thenReturn(Optional.of(new User()));
        when(friendshipRepository.save(any(Friendship.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        stubCommonModelMapper();


        friendshipService.get(1L);
        friendshipService.modify(1L, dto);
        friendshipService.delete(1L);

        verify(friendshipRepository, times(3)).findById(1L);
        verify(friendshipRepository, times(1)).save(any(Friendship.class));
        verify(friendshipRepository, times(1)).deleteById(1L);
    }
}
