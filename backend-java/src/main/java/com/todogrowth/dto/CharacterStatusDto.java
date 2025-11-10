package com.todogrowth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterStatusDto {
    private Integer level;
    private Integer experience;
    private Integer requiredExperience; // 항상 20
    private Double experienceProgress; // experience / 20 * 100
}
