package org.svalero.memesconclase.repository;

import org.svalero.memesconclase.domain.Reaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReactionRepository extends CrudRepository<Reaction, Long> {

    List<Reaction> findAll();
    List<Reaction> findByUser_Id(Long userId);
    List<Reaction> findByPublication_Id(Long publicationId);
    List<Reaction> findByType(Reaction.ReactionType type);
    List<Reaction> findByTypeAndPublication_Id(Reaction.ReactionType type, Long publicationId);

    @Query("""
           SELECT DISTINCT r
           FROM Reaction r
             JOIN r.user u
             JOIN r.publication p
           WHERE (:type          IS NULL OR r.type = :type)
             AND (:publicationId IS NULL OR p.id = :publicationId)
             AND (:userId        IS NULL OR u.id = :userId)
           """)
    List<Reaction> findByOptionalCriteria(@Param("type") Reaction.ReactionType type,
                                          @Param("publicationId") Long publicationId,
                                          @Param("userId") Long userId);
}
