package org.svalero.memesconclase.service;


import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.svalero.memesconclase.domain.dto.UserOutDto;
import org.svalero.memesconclase.exception.UserNotFoundException;
import org.svalero.memesconclase.domain.User;
import org.svalero.memesconclase.repository.UserRepository;


import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<UserOutDto> getAll() {
        List<User> allUsers = userRepository.findAll();
        List<UserOutDto> userOutDtos = modelMapper.map(allUsers, new TypeToken<List<UserOutDto>>() {}.getType());
        return userOutDtos;
    }

    public User get(long UserId) throws UserNotFoundException {
        return userRepository.findById(UserId).orElseThrow(UserNotFoundException::new);
    }

    public User add(User user) {
        return userRepository.save(user);
    }

    public void delete(long userId) throws UserNotFoundException {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userRepository.deleteById(userId);
    }
}
