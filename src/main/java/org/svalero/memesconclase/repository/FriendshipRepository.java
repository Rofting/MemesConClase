package org.svalero.memesconclase.repository;

import org.svalero.memesconclase.domain.Friendship;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FriendshipRepository extends CrudRepository<Friendship, Long> {

    List<Friendship> findAll();
    List<Friendship> findByUserId(Long userId);
    List<Friendship> findByFriendId(Long friendId);
    List<Friendship> findByStatus(String status);
}
