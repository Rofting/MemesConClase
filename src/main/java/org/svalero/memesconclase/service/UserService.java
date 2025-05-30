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
import java.util.Optional;

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
            userList = userRepository.findByEmail(email)
                    .map(List::of)
                    .orElse(List.of());
        } else if (email.isEmpty()) {
            userList = userRepository.findByName(name);
        } else {
            userList = userRepository.findByNameAndEmail(name, email);
        }

        return modelMapper.map(userList, new TypeToken<List<UserOutDto>>() {}.getType());
    }


    public User get(long userId) throws UserNotFoundException {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    public UserOutDto modify (long userId, UserInDto userInDto) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        modelMapper.map(userInDto, user);
        userRepository.save(user);
        return modelMapper.map(user, UserOutDto.class);
    }

    public UserOutDto add(UserInDto userInDto) {
        User user = modelMapper.map(userInDto, User.class);
        user = userRepository.save(user);
        return modelMapper.map(user, UserOutDto.class);
    }

    public void delete(long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }

    public UserOutDto login(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (user.getPassword().equals(password)) {
                return new UserOutDto(user);
            }
        }

        return null;
    }


}
