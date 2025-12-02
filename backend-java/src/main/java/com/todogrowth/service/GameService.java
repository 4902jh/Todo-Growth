package com.todogrowth.service;

import com.todogrowth.dto.CharacterStatusDto;
import com.todogrowth.dto.CompleteTodoResponseDto;
import com.todogrowth.dto.FailTodoResponseDto;
import com.todogrowth.entity.Character;
import com.todogrowth.entity.Todo;
import com.todogrowth.entity.TodoLog;
import com.todogrowth.entity.User;
import com.todogrowth.repository.CharacterRepository;
import com.todogrowth.repository.TodoLogRepository;
import com.todogrowth.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {
    private final CharacterRepository characterRepository;
    private final TodoRepository todoRepository;
    private final TodoLogRepository todoLogRepository;
    private final com.todogrowth.repository.UserRepository userRepository;

    // 사용자의 캐릭터 조회 또는 생성
    @Transactional
    public Character getOrCreateCharacter(Long userId) {
        Optional<Character> characterOpt = characterRepository.findByUserId(userId);
        
        if (characterOpt.isEmpty()) {
            // User 조회 또는 생성
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                // User가 존재하지 않으면 생성
                try {
                    userRepository.createUserWithId(userId, "user" + userId, 
                        "user" + userId + "@example.com", "dummy");
                    user = userRepository.findById(userId).orElse(null);
                    log.info("새로운 User 생성: id={}, username={}", userId, "user" + userId);
                } catch (Exception e) {
                    log.warn("ID 직접 지정 실패, 자동 생성으로 전환: {}", e.getMessage());
                    user = new User();
                    user.setUsername("user" + userId);
                    user.setEmail("user" + userId + "@example.com");
                    user.setPasswordHash("dummy");
                    user = userRepository.save(user);
                    log.info("새로운 User 생성 (자동 ID): id={}, username={}", user.getId(), user.getUsername());
                }
            }
            
            // 캐릭터 생성
            Character character = new Character();
            character.setUser(user);
            character.setLevel(1);
            character.setExperience(0);
            
            return characterRepository.save(character);
        }
        
        return characterOpt.get();
    }

    // Todo 완료 처리 (순서도: EXP +5, EXP >= 20이면 레벨업)
    @Transactional
    public CompleteTodoResponseDto completeTodo(Long userId, Long todoId) {
        LocalDate today = LocalDate.now();
        
        // Todo 존재 확인
        Todo todo = todoRepository.findById(todoId)
            .orElseThrow(() -> new IllegalArgumentException(
                String.format("Todo를 찾을 수 없습니다. (todoId: %d)", todoId)));
        
        if (todo.getUser() == null || !todo.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException(
                String.format("권한이 없습니다. (userId: %d, todo의 userId: %s)", 
                    userId, todo.getUser() != null ? todo.getUser().getId() : "null"));
        }
        
        // 이미 오늘 완료했는지 확인
        Optional<TodoLog> existingLogOpt = todoLogRepository.findByTodoIdAndDate(todoId, today);
        
        if (existingLogOpt.isPresent() && existingLogOpt.get().getCompleted()) {
            throw new IllegalStateException("이미 오늘 완료한 Todo입니다.");
        }
        
        // 캐릭터 정보 가져오기
        Character character = getOrCreateCharacter(userId);
        
        // Todo 완료 처리 (EXP +5)
        Character.CompleteResult result = character.completeTodo();
        
        // 데이터베이스 저장
        characterRepository.save(character);
        
        // TodoLog 생성 또는 업데이트
        TodoLog todoLog;
        if (existingLogOpt.isPresent()) {
            todoLog = existingLogOpt.get();
            todoLog.setCompleted(true);
        } else {
            todoLog = new TodoLog();
            todoLog.setTodo(todo);
            todoLog.setUser(todo.getUser());
            todoLog.setDate(today);
            todoLog.setCompleted(true);
        }
        todoLogRepository.save(todoLog);
        
        // 응답 DTO 생성
        CharacterStatusDto characterDto = toCharacterStatusDto(character);
        
        return new CompleteTodoResponseDto(
            true,
            result.getLeveledUp() ? "레벨업했습니다!" : "Todo를 완료했습니다!",
            result.getExperienceGained(),
            result.getNewLevel(),
            character.getExperience(),
            result.getLeveledUp(),
            characterDto
        );
    }

    // Todo 실패 처리 (순서도: EXP -5, EXP >= 20이면 레벨업)
    @Transactional
    public FailTodoResponseDto failTodo(Long userId, Long todoId) {
        LocalDate today = LocalDate.now();
        
        // Todo 존재 확인
        Todo todo = todoRepository.findById(todoId)
            .orElseThrow(() -> new IllegalArgumentException("Todo를 찾을 수 없습니다."));
        
        if (todo.getUser() == null || !todo.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
        
        // 캐릭터 정보 가져오기
        Character character = getOrCreateCharacter(userId);
        
        // Todo 실패 처리 (EXP -5)
        Character.FailResult result = character.failTodo();
        
        // 데이터베이스 저장
        characterRepository.save(character);
        
        // TodoLog 생성 또는 업데이트
        Optional<TodoLog> existingLogOpt = todoLogRepository.findByTodoIdAndDate(todoId, today);
        TodoLog todoLog;
        if (existingLogOpt.isPresent()) {
            todoLog = existingLogOpt.get();
            todoLog.setCompleted(false);
        } else {
            todoLog = new TodoLog();
            todoLog.setTodo(todo);
            todoLog.setUser(todo.getUser());
            todoLog.setDate(today);
            todoLog.setCompleted(false);
        }
        todoLogRepository.save(todoLog);
        
        // 응답 DTO 생성
        CharacterStatusDto characterDto = toCharacterStatusDto(character);
        
        return new FailTodoResponseDto(
            true,
            result.getLeveledUp() 
                ? "레벨업했습니다!" 
                : "Todo를 달성하지 못했습니다.",
            result.getExperienceLost(),
            result.getNewExperience(),
            result.getNewLevel(),
            result.getLeveledUp(),
            characterDto
        );
    }

    // 일일 실패 체크 (스케줄러에서 호출)
    @Transactional
    public List<FailTodoResponseDto> checkDailyFailures() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        
        // 어제 완료되지 않은 Todo 찾기
        List<Object[]> incompleteTodos = todoLogRepository.findIncompleteTodosByDate(yesterday);
        
        List<FailTodoResponseDto> results = new ArrayList<>();
        
        for (Object[] row : incompleteTodos) {
            Long userId = (Long) row[0];
            Long todoId = (Long) row[1];
            
            try {
                FailTodoResponseDto result = failTodo(userId, todoId);
                results.add(result);
                log.info("Todo 실패 처리 완료: userId={}, todoId={}", userId, todoId);
            } catch (Exception e) {
                log.error("Todo 실패 처리 중 오류: todoId={}, error={}", todoId, e.getMessage());
            }
        }
        
        return results;
    }

    // 캐릭터 상태 조회
    @Transactional
    public CharacterStatusDto getCharacterStatus(Long userId) {
        Character character = getOrCreateCharacter(userId);
        return toCharacterStatusDto(character);
    }

    // Character 엔티티를 DTO로 변환
    private CharacterStatusDto toCharacterStatusDto(Character character) {
        Integer requiredExp = Character.REQUIRED_EXP_FOR_LEVEL_UP; // 항상 20
        Double expProgress = requiredExp > 0 
            ? (character.getExperience().doubleValue() / requiredExp) * 100 
            : 0.0;
        
        return new CharacterStatusDto(
            character.getLevel(),
            character.getExperience(),
            requiredExp,
            expProgress
        );
    }
}
