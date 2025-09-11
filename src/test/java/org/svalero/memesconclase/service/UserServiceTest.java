package org.svalero.memesconclase.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.svalero.memesconclase.domain.User;
import org.svalero.memesconclase.domain.dto.UserInDto;
import org.svalero.memesconclase.domain.dto.UserOutDto;
import org.svalero.memesconclase.exception.UserNotFoundException;
import org.svalero.memesconclase.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTest {

    @Mock UserRepository userRepository;
    @Mock ModelMapper modelMapper;

    @InjectMocks UserService userService;

    UserInDto inDto;
    User entity;
    UserOutDto outDto;

    @BeforeEach
    void setUp() {
        inDto = new UserInDto();
        inDto.setName("Ana");
        inDto.setEmail("ana@test.com");
        inDto.setPassword("pwd");
        inDto.setActive(true);

        entity = new User();
        outDto = mock(UserOutDto.class);

        when(modelMapper.map(any(UserInDto.class), eq(User.class))).thenReturn(entity);
        when(modelMapper.map(any(User.class), eq(UserOutDto.class))).thenReturn(outDto);
        doAnswer(inv -> inv.getArgument(1)).when(modelMapper).map(any(UserInDto.class), any(User.class));
    }

    @Test
    void add_and_get_ok() throws Exception {
        when(userRepository.save(entity)).thenReturn(entity);
        assertNotNull(userService.add(inDto));

        when(userRepository.findById(1L)).thenReturn(Optional.of(entity));
        assertNotNull(userService.get(1L));
    }

    @Test
    void modify_ok_and_404() throws Exception {
        User existing = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(existing)).thenReturn(existing);
        assertNotNull(userService.modify(1L, inDto));

        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.get(99L));
    }

    @Test
    void delete_ok_and_404() throws Exception {
        User existing = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(userRepository).delete(existing);
        userService.delete(1L);
        verify(userRepository).delete(existing);

        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.delete(99L));
    }

    @Test
    void get_404() throws Exception {
        when(userRepository.findById(42L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.get(42L));
    }
}
