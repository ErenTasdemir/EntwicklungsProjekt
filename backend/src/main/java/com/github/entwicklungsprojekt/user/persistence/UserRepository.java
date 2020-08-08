package com.github.entwicklungsprojekt.user.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * {@link JpaRepository} for persisting {@link User}s.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long userId);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

}
