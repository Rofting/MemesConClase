package org.svalero.memesconclase.repository;

import org.svalero.memesconclase.domain.Reaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReactionRepository extends CrudRepository<Reaction, Long> {

    List<Reaction> findAll();

    List<Reaction> findByUser_Id(Long userId);

    List<Reaction> findByPublication_Id(Long publicationId);

    List<Reaction> findByType(Reaction.ReactionType type);

    List<Reaction> findByTypeAndPublication_Id(Reaction.ReactionType type, Long publicationId);
}
