package com.todogrowth.controller;

import com.todogrowth.entity.Todo;
import com.todogrowth.repository.TodoRepository;
import com.todogrowth.repository.TodoLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class TodoController {
    private final TodoRepository todoRepository;
    private final TodoLogRepository todoLogRepository;

    // 사용자의 모든 Todo 조회 (오늘 완료 여부 포함)
    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserTodos(@PathVariable Long userId) {
        try {
            List<Todo> todos = todoRepository.findByUserId(userId);
            LocalDate today = LocalDate.now();
            
            // 각 Todo에 오늘 완료 여부 추가
            List<TodoWithStatus> todosWithStatus = todos.stream()
                .map(todo -> {
                    boolean todayCompleted = todoLogRepository
                        .findByTodoIdAndDate(todo.getId(), today)
                        .map(log -> log.getCompleted())
                        .orElse(false);
                    
                    return new TodoWithStatus(
                        todo.getId(),
                        todo.getTitle(),
                        todo.getDescription(),
                        todo.getIsActive(),
                        todayCompleted
                    );
                })
                .collect(Collectors.toList());
            
            return ResponseEntity.ok()
                .body(new ApiResponse<>(true, todosWithStatus, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, null, e.getMessage()));
        }
    }

    // Todo 생성
    @PostMapping("/users/{userId}")
    public ResponseEntity<?> createTodo(
            @PathVariable Long userId,
            @RequestBody CreateTodoRequest request) {
        try {
            Todo todo = new Todo();
            com.todogrowth.entity.User user = new com.todogrowth.entity.User();
            user.setId(userId);
            todo.setUser(user);
            todo.setTitle(request.getTitle());
            todo.setDescription(request.getDescription());
            todo.setIsActive(true);
            
            todo = todoRepository.save(todo);
            return ResponseEntity.ok()
                .body(new ApiResponse<>(true, todo, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, null, e.getMessage()));
        }
    }

    // API 응답 래퍼 클래스
    @lombok.Data
    @lombok.AllArgsConstructor
    public static class ApiResponse<T> {
        private Boolean success;
        private T data;
        private String error;
    }

    // Todo 생성 요청 DTO
    @lombok.Data
    public static class CreateTodoRequest {
        private String title;
        private String description;
    }

    // Todo 정보 + 오늘 완료 여부 DTO
    @lombok.Data
    @lombok.AllArgsConstructor
    public static class TodoWithStatus {
        private Long id;
        private String title;
        private String description;
        private Boolean isActive;
        private Boolean todayCompleted;
    }
}

