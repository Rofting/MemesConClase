package org.svalero.memesconclase.unitary;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.svalero.memesconclase.domain.User;
import org.svalero.memesconclase.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByName_returnsCorrectUser() {
        User user = new User();
        user.setName("Juan");
        user.setEmail("juan@test.com");
        user.setPassword("123456");
        user.setBirthDate(LocalDate.now());
        entityManager.persistAndFlush(user);


        List<User> foundUsers = userRepository.findByName("Juan");

        assertThat(foundUsers).hasSize(1);
        assertThat(foundUsers.get(0).getEmail()).isEqualTo("juan@test.com");
    }

    @Test
    void findByEmail_returnsCorrectUser() {

        User user = new User();
        user.setName("Maria");
        user.setEmail("maria@test.com");
        user.setPassword("123456");
        user.setBirthDate(LocalDate.now());
        entityManager.persistAndFlush(user);

        Optional<User> foundUser = userRepository.findByEmail("maria@test.com");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("Maria");
    }
}