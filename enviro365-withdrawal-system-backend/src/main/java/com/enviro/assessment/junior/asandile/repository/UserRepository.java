package com.enviro.assessment.junior.asandile.repository;

import com.enviro.assessment.junior.asandile.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for User entity operations
 *
 * @author Asandile
 * @version 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Find user by username
     *
     * @param username the username
     * @return Optional containing user if found
     */
    Optional<User> findByUsername(String username);

    /**
     * Check if user exists by username
     *
     * @param username the username
     * @return true if exists
     */
    boolean existsByUsername(String username);

    /**
     * Find user by email
     *
     * @param email the email
     * @return Optional containing user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user with investor details loaded
     *
     * @param username the username
     * @return Optional containing user with investor
     */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.investor WHERE u.username = :username")
    Optional<User> findByUsernameWithInvestor(@Param("username") String username);
}