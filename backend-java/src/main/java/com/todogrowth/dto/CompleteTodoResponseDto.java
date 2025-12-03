package com.todogrowth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompleteTodoResponseDto {
    private Boolean success;
    private String message;
    private Integer experienceGained;
    private Integer currentLevel;
    private Integer currentExperience;
    private Boolean leveledUp;
    private CharacterStatusDto character;
}

