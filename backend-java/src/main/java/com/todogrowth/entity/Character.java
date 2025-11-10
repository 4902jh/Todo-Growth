package com.todogrowth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "characters")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer level = 1;

    @Column(nullable = false)
    private Integer experience = 0;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // 레벨업에 필요한 경험치 (순서도: EXP >= 20)
    public static final Integer REQUIRED_EXP_FOR_LEVEL_UP = 20;

    // Todo 완료 처리
    public CompleteResult completeTodo() {
        Integer oldLevel = this.level;
        
        // EXP +5
        this.experience += 5;
        
        // EXP >= 20이면 레벨업
        Boolean leveledUp = false;
        if (this.experience >= REQUIRED_EXP_FOR_LEVEL_UP) {
            levelUp();
            leveledUp = true;
        }
        
        return new CompleteResult(
            5, // 획득한 경험치
            this.level,
            leveledUp
        );
    }

    // Todo 실패 처리
    public FailResult failTodo() {
        Integer oldLevel = this.level;
        
        // EXP -5 (최소 0)
        this.experience = Math.max(0, this.experience - 5);
        
        // EXP >= 20이면 레벨업 (실패해도 경험치가 20 이상이면 레벨업 가능)
        Boolean leveledUp = false;
        if (this.experience >= REQUIRED_EXP_FOR_LEVEL_UP) {
            levelUp();
            leveledUp = true;
        }
        
        return new FailResult(
            -5, // 잃은 경험치
            this.experience,
            this.level,
            leveledUp
        );
    }

    // 레벨업 처리 (순서도: 레벨 +1, EXP = 0)
    private void levelUp() {
        this.level += 1;
        this.experience = 0; // EXP 초기화
    }

    // Todo 완료 처리 결과를 담는 내부 클래스
    @Data
    @AllArgsConstructor
    public static class CompleteResult {
        private Integer experienceGained;
        private Integer newLevel;
        private Boolean leveledUp;
    }

    // Todo 실패 처리 결과를 담는 내부 클래스
    @Data
    @AllArgsConstructor
    public static class FailResult {
        private Integer experienceLost;
        private Integer newExperience;
        private Integer newLevel;
        private Boolean leveledUp;
    }
}
