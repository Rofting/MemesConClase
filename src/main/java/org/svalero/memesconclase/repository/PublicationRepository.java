package org.svalero.memesconclase.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.svalero.memesconclase.domain.Publication;

import java.util.List;

@Repository
public interface PublicationRepository extends CrudRepository<Publication, Long> {

    List<Publication> findAll();
    List<Publication> findByPrivacy(Publication.Privacy privacy);
    List<Publication> findByTypeContent(Publication.ContentType typeContent);
    List<Publication> findByTypeContentAndPrivacy(Publication.ContentType typeContent, Publication.Privacy privacy);
    List<Publication> findByUser_Id(long id);

    @Query("""
           SELECT DISTINCT p
           FROM Publication p
             JOIN p.user u
           WHERE (:typeContent IS NULL OR p.typeContent = :typeContent)
             AND (:privacy     IS NULL OR p.privacy = :privacy)
             AND (:userId      IS NULL OR u.id = :userId)
           """)
    List<Publication> findByOptionalCriteria(@Param("typeContent") Publication.ContentType typeContent,
                                             @Param("privacy") Publication.Privacy privacy,
                                             @Param("userId") Long userId);
}
