package com.todogrowth.repository;

import com.todogrowth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    
    // ID를 직접 지정하여 User 생성 (테스트용)
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO users (id, username, email, password_hash, created_at) " +
                   "VALUES (:id, :username, :email, :passwordHash, NOW()) " +
                   "ON DUPLICATE KEY UPDATE username = username", nativeQuery = true)
    void createUserWithId(@Param("id") Long id, 
                         @Param("username") String username,
                         @Param("email") String email,
                         @Param("passwordHash") String passwordHash);
}
