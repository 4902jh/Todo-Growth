package com.todogrowth.repository;

import com.todogrowth.entity.Todo;
import com.todogrowth.entity.TodoLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TodoLogRepository extends JpaRepository<TodoLog, Long> {
    Optional<TodoLog> findByTodoAndDate(Todo todo, LocalDate date);
    
    @Query("SELECT tl FROM TodoLog tl WHERE tl.todo.id = :todoId AND tl.date = :date")
    Optional<TodoLog> findByTodoIdAndDate(@Param("todoId") Long todoId, @Param("date") LocalDate date);
    
    @Query("SELECT DISTINCT t.user.id, t.id FROM Todo t " +
           "LEFT JOIN TodoLog tl ON t.id = tl.todo.id AND tl.date = :date " +
           "WHERE t.isActive = true AND (tl.id IS NULL OR tl.completed = false)")
    List<Object[]> findIncompleteTodosByDate(@Param("date") LocalDate date);
}

