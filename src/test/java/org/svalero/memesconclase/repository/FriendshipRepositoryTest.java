package org.svalero.memesconclase.repository;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.svalero.memesconclase.domain.Friendship;
import org.svalero.memesconclase.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(locations = "classpath:application-test.properties")
class FriendshipRepositoryTest {

    @Autowired private FriendshipRepository friendshipRepository;
    @Autowired private UserRepository userRepository;

    @Test void crud_basico() {
        User u1 = new User(); u1.setName("A"); u1.setEmail("a@a.com"); u1.setPassword("p");
        u1.setBirthDate(LocalDate.of(1990,1,1)); u1.setActive(true); u1.setSex("Male"); u1 = userRepository.save(u1);

        User u2 = new User(); u2.setName("B"); u2.setEmail("b@b.com"); u2.setPassword("p");
        u2.setBirthDate(LocalDate.of(1991,1,1)); u2.setActive(true); u2.setSex("Female"); u2 = userRepository.save(u2);

        Friendship f = new Friendship(); f.setUser(u1); f.setFriend(u2); f.setStatus(Friendship.Status.ACCEPTED);
        Friendship sf = friendshipRepository.save(f);

        Optional<Friendship> found = friendshipRepository.findById(sf.getId());
        assertTrue(found.isPresent());

        friendshipRepository.delete(sf);
        assertFalse(friendshipRepository.findById(sf.getId()).isPresent());
    }
}

