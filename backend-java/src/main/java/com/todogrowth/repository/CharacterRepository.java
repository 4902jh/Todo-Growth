package com.todogrowth.repository;

import com.todogrowth.entity.Character;
import com.todogrowth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
    Optional<Character> findByUser(User user);
    Optional<Character> findByUserId(Long userId);
}

