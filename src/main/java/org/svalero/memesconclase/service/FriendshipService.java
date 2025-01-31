package org.svalero.memesconclase.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.svalero.memesconclase.domain.Friendship;
import org.svalero.memesconclase.domain.dto.FriendshipInDto;
import org.svalero.memesconclase.domain.dto.FriendshipOutDto;
import org.svalero.memesconclase.exception.FriendshipNotFoundException;
import org.svalero.memesconclase.repository.FriendshipRepository;

import java.util.List;

@Service
public class FriendshipService {

    @Autowired
    private FriendshipRepository friendshipRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<FriendshipOutDto> getAll(String status, Long userId) {
        List<Friendship> friendshipList;

        if (status.isEmpty() && userId == null) {
            friendshipList = friendshipRepository.findAll();
        } else if (status.isEmpty()) {
            friendshipList = friendshipRepository.findByUserId(userId);
        } else if (userId == null) {
            friendshipList = friendshipRepository.findByStatus(status);
        } else {
            friendshipList = friendshipRepository.findByUserIdAndStatus(userId, status);
        }

        return modelMapper.map(friendshipList, new TypeToken<List<FriendshipOutDto>>() {}.getType());
    }


    public Friendship get(long friendshipId) throws FriendshipNotFoundException {
        return friendshipRepository.findById(friendshipId)
                .orElseThrow(FriendshipNotFoundException::new);
    }

    public FriendshipOutDto add(FriendshipInDto friendshipInDto) {
        Friendship friendship = modelMapper.map(friendshipInDto, Friendship.class);
        friendship = friendshipRepository.save(friendship);
        return modelMapper.map(friendship, FriendshipOutDto.class);
    }

    public void delete(long friendshipId) throws FriendshipNotFoundException {
        friendshipRepository.findById(friendshipId).orElseThrow(FriendshipNotFoundException::new);
        friendshipRepository.deleteById(friendshipId);
    }

    public FriendshipOutDto modify(long friendshipId, FriendshipInDto friendshipInDto) throws FriendshipNotFoundException{
        Friendship friendship = friendshipRepository.findById(friendshipId).orElseThrow(FriendshipNotFoundException::new);
        modelMapper.map(friendshipInDto, friendship);
        friendshipRepository.save(friendship);
        return modelMapper.map(friendship, FriendshipOutDto.class);
    }
}
