package org.svalero.memesconclase.repository;

import org.svalero.memesconclase.domain.Comment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    List<Comment> findAll();
    List<Comment> findByUserId(Long userId);
    List<Comment> findByPublicationId(Long publicationId);
    List<Comment> findByContentContaining(String keyword); // Para buscar comentarios por palabra clave
}
