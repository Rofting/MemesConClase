package org.svalero.memesconclase.repository;

import org.springframework.data.repository.CrudRepository;
import org.svalero.memesconclase.model.Publication;

import java.util.List;

public interface PublicationRepository extends CrudRepository<Publication, Long> {
    List<Publication> findAll();
    List<Publication> findByUserId(long userId);
}
