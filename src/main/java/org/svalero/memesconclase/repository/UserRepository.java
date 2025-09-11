package org.svalero.memesconclase.repository;

import org.svalero.memesconclase.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findAll();
    List<User> findByName(String name);
    Optional<User> findByEmail(String email);

    @Query("FROM User WHERE name LIKE %:searchTerm% OR email LIKE %:searchTerm%")
    List<User> findByNameOrEmail(String searchTerm);

    Optional<User> findByNameAndPassword(String name, String password);

    @Query("""
           SELECT u
           FROM User u
           WHERE (:search IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%'))
                               OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')))
             AND (:active IS NULL OR u.active = :active)
             AND (:sex    IS NULL OR u.sex = :sex)
           """)
    List<User> findByOptionalCriteria(String search, Boolean active, String sex);
}
