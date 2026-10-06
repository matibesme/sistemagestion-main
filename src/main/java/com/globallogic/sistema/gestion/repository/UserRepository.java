package com.globallogic.sistema.gestion.repository;

import com.globallogic.sistema.gestion.model.Roles;
import com.globallogic.sistema.gestion.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds a user by their username.
     * @param username The username to search for.
     * @return An Optional containing the user if found.
     */
    Optional<User> findByName(String username);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    List<User> findByRolesContaining(Roles role);
    Page<User> findByRolesContaining(Roles role, Pageable pageable);
}