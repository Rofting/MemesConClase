package org.svalero.memesconclase.repository;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.svalero.memesconclase.domain.Publication;
import org.svalero.memesconclase.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(locations = "classpath:application-test.properties")
class PublicationRepositoryTest {

    @Autowired private PublicationRepository publicationRepository;
    @Autowired private UserRepository userRepository;

    @Test void crud_basico() {
        User u = new User();
        u.setName("Bob"); u.setEmail("b@b.com"); u.setPassword("p");
        u.setBirthDate(LocalDate.of(1991,1,1)); u.setActive(true); u.setSex("Male");
        User su = userRepository.save(u);

        Publication p = new Publication();
        p.setUser(su); p.setContent("hola");
        p.setTypeContent(Publication.ContentType.TEXT);
        p.setPrivacy(Publication.Privacy.PUBLIC);
        p.setLatitude(1.0); p.setLongitude(2.0);
        Publication sp = publicationRepository.save(p);

        Optional<Publication> found = publicationRepository.findById(sp.getId());
        assertTrue(found.isPresent());

        publicationRepository.delete(sp);
        assertFalse(publicationRepository.findById(sp.getId()).isPresent());
    }
}

