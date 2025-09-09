package org.svalero.memesconclase.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.svalero.memesconclase.domain.Friendship;
import org.svalero.memesconclase.domain.User;
import org.svalero.memesconclase.domain.dto.FriendshipInDto;
import org.svalero.memesconclase.domain.dto.FriendshipOutDto;
import org.svalero.memesconclase.domain.dto.UserOutDto;
import org.svalero.memesconclase.exception.FriendshipNotFoundException;
import org.svalero.memesconclase.exception.UserNotFoundException;
import org.svalero.memesconclase.repository.FriendshipRepository;
import org.svalero.memesconclase.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class FriendshipService {

    @Autowired private FriendshipRepository friendshipRepository;
    @Autowired private ModelMapper modelMapper;
    @Autowired private UserRepository userRepository;

    public List<FriendshipOutDto> getAll(Long userId, String statusStr, String startDateStr, String endDateStr) {
        modelMapper.typeMap(User.class, UserOutDto.class);

        Friendship.Status status = null;
        if (statusStr != null && !statusStr.isBlank()) {
            try { status = Friendship.Status.valueOf(statusStr.toUpperCase()); } catch (IllegalArgumentException ignored) {}
        }

        LocalDateTime startDate = (startDateStr == null || startDateStr.isBlank())
                ? null : LocalDateTime.parse(startDateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime endDate = (endDateStr == null || endDateStr.isBlank())
                ? null : LocalDateTime.parse(endDateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        var friendshipList = friendshipRepository.findByOptionalCriteria(userId, status, startDate, endDate);

        return modelMapper.map(friendshipList, new TypeToken<List<FriendshipOutDto>>() {}.getType());
    }

    public Friendship get(long friendshipId) throws FriendshipNotFoundException {
        return friendshipRepository.findById(friendshipId)
                .orElseThrow(FriendshipNotFoundException::new);
    }

    public FriendshipOutDto add(FriendshipInDto friendshipInDto) throws UserNotFoundException {
        Long userId = friendshipInDto.getUserId();
        Long friendId = friendshipInDto.getFriendId();
        if (userId.equals(friendId)) {
            throw new IllegalArgumentException("Un usuario no puede ser amigo de sí mismo.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario con ID " + userId + " no encontrado"));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new UserNotFoundException("Usuario amigo con ID " + friendId + " no encontrado"));

        Friendship newFriendship = new Friendship();
        newFriendship.setUser(user);
        newFriendship.setFriend(friend);

        Friendship saved = friendshipRepository.save(newFriendship);
        // El DTO ahora incluye user y friend completos
        modelMapper.typeMap(User.class, UserOutDto.class);
        return modelMapper.map(saved, FriendshipOutDto.class);
    }

    public void delete(long friendshipId) throws FriendshipNotFoundException {
        friendshipRepository.findById(friendshipId).orElseThrow(FriendshipNotFoundException::new);
        friendshipRepository.deleteById(friendshipId);
    }

    public FriendshipOutDto modify(long friendshipId, FriendshipInDto friendshipInDto) throws FriendshipNotFoundException {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(FriendshipNotFoundException::new);

        if (friendshipInDto.getStatus() != null) {
            friendship.setStatus(Friendship.Status.valueOf(friendshipInDto.getStatus().toUpperCase()));
        }
        Friendship saved = friendshipRepository.save(friendship);

        modelMapper.typeMap(User.class, UserOutDto.class);
        return modelMapper.map(saved, FriendshipOutDto.class);
    }
}

