package org.svalero.memesconclase.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.svalero.memesconclase.domain.Friendship;
import org.svalero.memesconclase.exception.FriendshipNotFoundException;
import org.svalero.memesconclase.repository.FriendshipRepository;

import java.util.List;

@Service
public class FriendshipService {

    @Autowired
    private FriendshipRepository friendshipRepository;

    public List<Friendship> getAll() {
        return friendshipRepository.findAll();
    }

    public Friendship get(long friendshipId) throws FriendshipNotFoundException {
        return friendshipRepository.findById(friendshipId)
                .orElseThrow(FriendshipNotFoundException::new);
    }

    public Friendship add(Friendship friendship) {
        return friendshipRepository.save(friendship);
    }

    public void delete(long friendshipId) throws FriendshipNotFoundException {
        friendshipRepository.findById(friendshipId).orElseThrow(FriendshipNotFoundException::new);
        friendshipRepository.deleteById(friendshipId);
    }
}
