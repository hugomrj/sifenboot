package org.sifenboot.shell.repository;

import org.sifenboot.shell.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Spring deduce: SELECT * FROM usuarios WHERE username = ?
    Optional<User> findByUsername(String username);
}