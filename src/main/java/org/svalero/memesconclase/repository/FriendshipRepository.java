package org.svalero.memesconclase.repository;

import org.svalero.memesconclase.domain.Friendship;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface FriendshipRepository extends CrudRepository<Friendship, Long> {

    List<Friendship> findAll();

    @Query("""
           SELECT f
           FROM Friendship f
             JOIN FETCH f.user u
             JOIN FETCH f.friend fr
           WHERE (:userId   IS NULL OR u.id = :userId OR fr.id = :userId)
             AND (:status   IS NULL OR f.status = :status)
             AND (:startDate IS NULL OR f.createdAt >= :startDate)
             AND (:endDate   IS NULL OR f.createdAt <= :endDate)
           """)
    List<Friendship> findByOptionalCriteria(
            @Param("userId") Long userId,
            @Param("status") Friendship.Status status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
