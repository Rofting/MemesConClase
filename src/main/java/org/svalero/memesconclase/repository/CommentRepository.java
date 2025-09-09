package org.svalero.memesconclase.repository;

import org.svalero.memesconclase.domain.Comment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    List<Comment> findAll();
    List<Comment> findByContentContaining(String content);
    List<Comment> findByPublication_Id(Long publicationId);
    List<Comment> findByContentContainingAndPublication_Id(String content, Long publicationId);
}
