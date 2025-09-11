package org.svalero.memesconclase.repository;

import org.svalero.memesconclase.domain.Comment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    List<Comment> findAll();
    List<Comment> findByContentContaining(String content);
    List<Comment> findByPublication_Id(Long publicationId);
    List<Comment> findByContentContainingAndPublication_Id(String content, Long publicationId);

    @Query("""
           SELECT DISTINCT c
           FROM Comment c
             JOIN c.user u
             JOIN c.publication p
           WHERE (:content       IS NULL OR LOWER(c.content) LIKE LOWER(CONCAT('%', :content, '%')))
             AND (:publicationId IS NULL OR p.id = :publicationId)
             AND (:userId        IS NULL OR u.id = :userId)
           """)
    List<Comment> findByOptionalCriteria(@Param("content") String content,
                                         @Param("publicationId") Long publicationId,
                                         @Param("userId") Long userId);
}
