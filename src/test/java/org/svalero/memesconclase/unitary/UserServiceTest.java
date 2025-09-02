package org.svalero.memesconclase.unitary;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.svalero.memesconclase.domain.User;
import org.svalero.memesconclase.domain.dto.UserInDto;
import org.svalero.memesconclase.exception.UserNotFoundException;
import org.svalero.memesconclase.repository.UserRepository;
import org.svalero.memesconclase.service.UserService;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void get_UserExists_ReturnsUser() throws UserNotFoundException {

        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.get(1L);

        // Assert: Verificamos el resultado
        assertNotNull(foundUser);
        assertEquals("Test User", foundUser.getName());
        verify(userRepository).findById(1L); // Verificamos que se llamó al método del mock
    }

    @Test
    void get_UserDoesNotExist_ThrowsUserNotFoundException() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.get(1L);
        });
        verify(userRepository).findById(1L);
    }

    @Test
    void add_ValidUser_SavesAndReturnsDto() {

        UserInDto userInDto = new UserInDto();
        userInDto.setName("New User");
        userInDto.setEmail("new@test.com");
        userInDto.setPassword("password123");
        userInDto.setBirthDate(LocalDate.now());
        userInDto.setActive(true);

        User user = new User();
        user.setName(userInDto.getName());

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("New User");

        when(modelMapper.map(userInDto, User.class)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);

        userService.add(userInDto);

        verify(userRepository).save(any(User.class));
    }


    @Test
    void delete_UserExists_DeletesUser() throws UserNotFoundException {

        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        userService.delete(1L);

        verify(userRepository).delete(user);
    }

    @Test
    void delete_UserDoesNotExist_ThrowsUserNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.delete(1L);
        });
        verify(userRepository, never()).delete(any(User.class)); // Verificamos que delete NUNCA fue llamado
    }
}