package it.mikedmc.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.mikedmc.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);

    // Trova l'utente per email o username
    User findByEmailOrUsername(String email, String username);
    
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userRoles WHERE (u.username = :username OR u.email = :username) AND u.confirmed = true")
    User findByUsernameOrEmailAndConfirmed(@Param("username") String username);
    
    @Query("SELECT u FROM User u WHERE (u.username = :username OR u.email = :username)")
    User findByUsernameOrEmail(@Param("username") String username);
    
    List<User> findByConfirmedTrue();

    List<User> findByConfirmedFalse();
    
    long countByConfirmed(boolean confirmed);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdDate BETWEEN :startDate AND :endDate")
    long countUsersCreatedBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT u FROM User u WHERE u.createdDate BETWEEN :startDate AND :endDate")
    List<User> findUsersCreatedBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.lastLoginDate BETWEEN :startDate AND :endDate")
    long countActiveUsers(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    

    @Query("SELECT u FROM User u WHERE u.lastLoginDate BETWEEN :startDate AND :endDate")
    List<User> findActiveUsers(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}