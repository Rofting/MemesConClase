package org.svalero.memesconclase.service;


import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.svalero.memesconclase.domain.dto.UserInDto;
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

    public List<UserOutDto> getAll(String name, String email) {
        List<User> userList;

        if (name.isEmpty() && email.isEmpty()) {
            userList = userRepository.findAll();
        } else if (name.isEmpty()) {
            userList = userRepository.findByEmail(email);
        } else if (email.isEmpty()) {
            userList = userRepository.findByName(name);
        }else {
            userList = userRepository.findByNameAndEmail(name, email);
        }
        return modelMapper.map(userList, new TypeToken<List<UserOutDto>>() {}.getType());
    }

    public User get(long UserId) throws UserNotFoundException {
        return userRepository.findById(UserId).orElseThrow(UserNotFoundException::new);
    }

    public UserOutDto modify (long userId, UserInDto userInDto) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        modelMapper.map(userInDto, user);
        userRepository.save(user);
        return modelMapper.map(user, UserOutDto.class);
    }

    public User add(User user) {
        return userRepository.save(user);
    }

    public void delete(long userId) throws UserNotFoundException {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userRepository.deleteById(userId);
    }
}
