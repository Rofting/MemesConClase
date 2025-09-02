package org.svalero.memesconclase.unitary;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.svalero.memesconclase.domain.Comment;
import org.svalero.memesconclase.domain.Publication;
import org.svalero.memesconclase.domain.User;
import org.svalero.memesconclase.repository.CommentRepository;
import org.svalero.memesconclase.repository.PublicationRepository;
import org.svalero.memesconclase.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PublicationRepository publicationRepository;

    @Test
    void shouldSaveAndFindByContent() {
        // Preparar usuario
        User user = new User();
        user.setName("Juan");
        user.setEmail("juan@mail.com");
        user.setPassword("1234");
        user.setBirthDate(LocalDate.of(2000, 1, 1));
        user.setActive(true);
        user = userRepository.save(user);

        // Preparar publicación
        Publication publication = new Publication();
        publication.setPrivacy(Publication.Privacy.PUBLIC);
        publication.setTypeContent(Publication.ContentType.IMAGE);
        publication.setUser(user);
        publication = publicationRepository.save(publication);

        // Preparar comentario
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPublication(publication);
        comment.setContent("This is a great post!");

        commentRepository.save(comment);

        // Test buscar por contenido
        List<Comment> found = commentRepository.findByContentContaining("great");

        assertFalse(found.isEmpty());
        assertEquals("This is a great post!", found.get(0).getContent());
    }

    @Test
    void shouldFindByPublicationId() {
        // Preparar usuario
        User user = new User();
        user.setName("Ana");
        user.setEmail("ana@mail.com");
        user.setPassword("5678");
        user.setBirthDate(LocalDate.of(1999, 5, 5));
        user.setActive(true);
        user = userRepository.save(user);

        // Preparar publicación
        Publication publication = new Publication();
        publication.setPrivacy(Publication.Privacy.PUBLIC);
        publication.setTypeContent(Publication.ContentType.TEXT);
        publication.setUser(user);
        publication = publicationRepository.save(publication);

        // Preparar comentario
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPublication(publication);
        comment.setContent("Interesting perspective!");

        commentRepository.save(comment);

        // Test buscar por publicationId
        List<Comment> found = commentRepository.findByPublicationId(publication.getId());

        assertFalse(found.isEmpty());
        assertEquals(publication.getId(), found.get(0).getPublication().getId());
    }
}
