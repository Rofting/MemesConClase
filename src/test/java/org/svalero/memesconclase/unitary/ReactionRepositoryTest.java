package org.svalero.memesconclase.unitary;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.svalero.memesconclase.domain.Publication;
import org.svalero.memesconclase.domain.Reaction;
import org.svalero.memesconclase.domain.User;
import org.svalero.memesconclase.repository.PublicationRepository;
import org.svalero.memesconclase.repository.ReactionRepository;
import org.svalero.memesconclase.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ReactionRepositoryTest {

    @Autowired
    private ReactionRepository reactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PublicationRepository publicationRepository;

    @Test
    void shouldSaveAndFindByType() {

        //Creamos un usuario
        User user = new User();
        user.setName("Juan");
        user.setEmail("Test@mail");
        user.setPassword("1234");
        user.setBirthDate(LocalDate.of(2000, 1, 1));
        user.setActive(true);
        user = userRepository.save(user);

        //Creamos una publication
        Publication publication = new Publication();
        publication.setPrivacy(Publication.Privacy.PUBLIC);
        publication.setTypeContent(Publication.ContentType.TEXT);
        publication.setUser(user);
        publication = publicationRepository.save(publication);

        // Creamos una reaccion
        Reaction reaction = new Reaction();
        reaction.setType(Reaction.ReactionType.LIKE);
        reaction.setPublication(publication);
        reaction.setUser(user);

        reactionRepository.save(reaction);

        List<Reaction> found = reactionRepository.findByType("like");

        assertFalse(found.isEmpty());
        assertEquals("like", found.get(0).getType());
    }
}
