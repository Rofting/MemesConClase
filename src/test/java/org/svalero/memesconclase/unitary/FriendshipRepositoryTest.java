package org.svalero.memesconclase.unitary;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.svalero.memesconclase.domain.Friendship;
import org.svalero.memesconclase.domain.User;
import org.svalero.memesconclase.repository.FriendshipRepository;
import org.svalero.memesconclase.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FriendshipRepositoryTest {

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndFindByStatus() {
        User user = new User();
        user.setName("Juan");
        user.setEmail("juan@mail.com");
        user.setPassword("1234");
        user.setBirthDate(LocalDate.of(2000, 1, 1));
        user.setActive(true);
        user = userRepository.save(user);

        User friend = new User();
        friend.setName("Ana");
        friend.setEmail("ana@mail.com");
        friend.setPassword("5678");
        friend.setBirthDate(LocalDate.of(1999, 5, 5));
        friend.setActive(true);
        friend = userRepository.save(friend);

        Friendship friendship = new Friendship();
        friendship.setUser(user);
        friendship.setFriend(friend);
        friendship.setStatus(Friendship.Status.ACCEPTED);

        friendshipRepository.save(friendship);

        List<Friendship> found = friendshipRepository.findByStatus("ACCEPTED");

        assertFalse(found.isEmpty());
        assertEquals(Friendship.Status.ACCEPTED, found.get(0).getStatus());
    }
}
