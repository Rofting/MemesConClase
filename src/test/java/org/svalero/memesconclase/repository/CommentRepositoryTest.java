package org.svalero.memesconclase.repository;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.svalero.memesconclase.domain.Comment;
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
class CommentRepositoryTest {

    @Autowired private CommentRepository commentRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PublicationRepository publicationRepository;

    @Test void crud_basico() {
        User u = new User(); u.setName("C"); u.setEmail("c@c.com"); u.setPassword("p");
        u.setBirthDate(LocalDate.of(1992,1,1)); u.setActive(true); u.setSex("Other");
        User su = userRepository.save(u);

        Publication p = new Publication(); p.setUser(su); p.setContent("post");
        p.setTypeContent(Publication.ContentType.TEXT); p.setPrivacy(Publication.Privacy.PUBLIC);
        p.setLatitude(1.0); p.setLongitude(2.0);
        Publication sp = publicationRepository.save(p);

        Comment c = new Comment(); c.setUser(su); c.setPublication(sp); c.setContent("nice one");
        Comment sc = commentRepository.save(c);

        Optional<Comment> found = commentRepository.findById(sc.getId());
        assertTrue(found.isPresent());

        commentRepository.delete(sc);
        assertFalse(commentRepository.findById(sc.getId()).isPresent());
    }
}


