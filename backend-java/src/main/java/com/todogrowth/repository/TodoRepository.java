package com.todogrowth.repository;

import com.todogrowth.entity.Todo;
import com.todogrowth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByUserAndIsActiveTrue(User user);
    List<Todo> findByUserId(Long userId);
}

