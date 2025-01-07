package org.svalero.memesconclase.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.svalero.memesconclase.exception.UserNotFoundException;
import org.svalero.memesconclase.model.User;
import org.svalero.memesconclase.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAll() {
        List<User> allUsers = userRepository.findAll();
        return allUsers;
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
