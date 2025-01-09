package org.svalero.memesconclase.repository;

import org.svalero.memesconclase.domain.Reaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReactionRepository extends CrudRepository<Reaction, Long> {

    List<Reaction> findAll();
    List<Reaction> findByUserId(Long userId);
    List<Reaction> findByPublicationId(Long publicationId);
    List<Reaction> findByType(String type);
}
