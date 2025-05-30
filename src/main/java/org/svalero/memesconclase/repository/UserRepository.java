package org.svalero.memesconclase.repository;

import org.svalero.memesconclase.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findAll();
    List<User> findByName(String name);
    Optional<User> findByEmail(String email);
    List<User> findByNameAndEmail(String name, String email);
    Optional<User> findByNameAndPassword(String name, String password);
}
