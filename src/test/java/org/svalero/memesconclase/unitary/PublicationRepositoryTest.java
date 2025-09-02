package org.svalero.memesconclase.unitary;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.svalero.memesconclase.domain.Publication;
import org.svalero.memesconclase.repository.PublicationRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PublicationRepositoryTest {

    @Autowired
    private PublicationRepository publicationRepository;

    @Test
    void shouldSaveAndFindByPrivacy() {
        Publication publication = new Publication();
        publication.setTypeContent(Publication.ContentType.IMAGE);
        publication.setPrivacy(Publication.Privacy.PUBLIC);

        publicationRepository.save(publication);

        List<Publication> found = publicationRepository.findByPrivacy("public");

        assertFalse(found.isEmpty());
        assertEquals("image", found.get(0).getTypeContent());
    }
}
