package org.svalero.memesconclase.repository;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.svalero.memesconclase.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(locations = "classpath:application-test.properties")
class UserRepositoryTest {

    @Autowired private UserRepository userRepository;

    @Test void crud_basico() {
        User u = new User();
        u.setName("Alice"); u.setEmail("a@a.com"); u.setPassword("p");
        u.setBirthDate(LocalDate.of(1990,1,1)); u.setActive(true); u.setSex("Female");
        User saved = userRepository.save(u);

        Optional<User> found = userRepository.findById(saved.getId());
        assertTrue(found.isPresent());

        userRepository.delete(saved);
        assertFalse(userRepository.findById(saved.getId()).isPresent());
    }
}
