package org.svalero.memesconclase.repository;

import org.springframework.data.repository.CrudRepository;
import org.svalero.memesconclase.domain.Publication;

import java.util.List;

public interface PublicationRepository extends CrudRepository<Publication, Long> {
    List<Publication> findAll();
    List<Publication> findByPrivacy(String privacy);
    List<Publication> findByTypeContent(String typeContent);
    List<Publication> findByTypeContentAndPrivacy(String typeContent, String privacy);

}
