package com.todogrowth.controller;

import com.todogrowth.dto.CharacterStatusDto;
import com.todogrowth.dto.CompleteTodoResponseDto;
import com.todogrowth.dto.FailTodoResponseDto;
import com.todogrowth.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    // 캐릭터 상태 조회
    @GetMapping("/users/{userId}/character")
    public ResponseEntity<?> getCharacterStatus(@PathVariable Long userId) {
        try {
            CharacterStatusDto status = gameService.getCharacterStatus(userId);
            return ResponseEntity.ok()
                .body(new ApiResponse<>(true, status, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, null, e.getMessage()));
        }
    }

    // Todo 완료 처리
    @PostMapping("/users/{userId}/todos/{todoId}/complete")
    public ResponseEntity<?> completeTodo(
            @PathVariable Long userId,
            @PathVariable Long todoId) {
        try {
            CompleteTodoResponseDto result = gameService.completeTodo(userId, todoId);
            return ResponseEntity.ok(result);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, null, e.getMessage()));
        }
    }

    // Todo 실패 처리 (수동)
    @PostMapping("/users/{userId}/todos/{todoId}/fail")
    public ResponseEntity<?> failTodo(
            @PathVariable Long userId,
            @PathVariable Long todoId) {
        try {
            FailTodoResponseDto result = gameService.failTodo(userId, todoId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, null, e.getMessage()));
        }
    }

    // 통계 정보 조회
    @GetMapping("/users/{userId}/stats")
    public ResponseEntity<?> getStats(@PathVariable Long userId) {
        try {
            CharacterStatusDto character = gameService.getCharacterStatus(userId);
            return ResponseEntity.ok()
                .body(new ApiResponse<>(true, character, null));
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
}
