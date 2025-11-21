package se.pbt.sudokusolver.core.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.pbt.sudokusolver.shared.dto.DifficultyDto;

import static org.junit.jupiter.api.Assertions.*;

class DifficultyMappingTest {

    @Test
    @DisplayName("Each Difficulty should map to and from its DTO representation")
    void enumConversion_isSymmetric() {
        for (Difficulty difficulty : Difficulty.values()) {
            DifficultyDto dto = Difficulty.toDto(difficulty);
            Difficulty restored = Difficulty.fromDto(dto);
            assertEquals(difficulty, restored);
        }
    }

    @Test
    @DisplayName("Mapping preserves clue fraction for Difficulty levels")
    void conversion_preservesClueFraction() {
        DifficultyDto dto = DifficultyDto.HARD;
        Difficulty difficulty = Difficulty.fromDto(dto);

        assertEquals(Difficulty.HARD.getClueFraction(), difficulty.getClueFraction());
    }
}
