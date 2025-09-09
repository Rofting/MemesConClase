package org.svalero.memesconclase.repository;

import org.svalero.memesconclase.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findAll();

    List<User> findByName(String name);

    Optional<User> findByEmail(String email);

    @Query("FROM User WHERE name LIKE %:searchTerm% OR email LIKE %:searchTerm%")
    List<User> findByNameOrEmail(String searchTerm);

    Optional<User> findByNameAndPassword(String name, String password);
}