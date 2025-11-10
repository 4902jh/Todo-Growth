package com.todogrowth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FailTodoResponseDto {
    private Boolean success;
    private String message;
    private Integer experienceLost;
    private Integer currentExperience;
    private Integer currentLevel;
    private Boolean leveledUp; // 실패해도 EXP >= 20이면 레벨업 가능
    private CharacterStatusDto character;
}

