package org.svalero.memesconclase.repository;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.svalero.memesconclase.domain.Publication;
import org.svalero.memesconclase.domain.Reaction;
import org.svalero.memesconclase.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(locations = "classpath:application-test.properties")
class ReactionRepositoryTest {

    @Autowired private ReactionRepository reactionRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PublicationRepository publicationRepository;

    @Test void crud_basico() {
        User u = new User(); u.setName("R"); u.setEmail("r@r.com"); u.setPassword("p");
        u.setBirthDate(LocalDate.of(1990,1,1)); u.setActive(true); u.setSex("Male");
        User su = userRepository.save(u);

        Publication p = new Publication(); p.setUser(su); p.setContent("pub");
        p.setTypeContent(Publication.ContentType.TEXT); p.setPrivacy(Publication.Privacy.PUBLIC);
        p.setLatitude(1.0); p.setLongitude(2.0);
        Publication sp = publicationRepository.save(p);

        Reaction r = new Reaction(); r.setUser(su); r.setPublication(sp); r.setType(Reaction.ReactionType.LIKE);
        Reaction sr = reactionRepository.save(r);

        Optional<Reaction> found = reactionRepository.findById(sr.getId());
        assertTrue(found.isPresent());

        reactionRepository.delete(sr);
        assertFalse(reactionRepository.findById(sr.getId()).isPresent());
    }
}


