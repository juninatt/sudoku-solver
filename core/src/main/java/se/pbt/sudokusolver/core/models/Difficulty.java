package se.pbt.sudokusolver.core.models;

import se.pbt.sudokusolver.shared.dto.DifficultyDto;
import se.pbt.sudokusolver.shared.localization.Localization;

import static se.pbt.sudokusolver.core.constants.CoreConstants.*;

/**
 * Represents the difficulty levels of a Sudoku puzzle.
 * Each difficulty level determines how many clues (pre-filled numbers) will be provided in the puzzle.
 */
public enum Difficulty {
    EASY(CLUE_FRACTION_EASY, I18N_DIFFICULTY_EASY),
    MEDIUM(CLUE_FRACTION_MEDIUM, I18N_DIFFICULTY_MEDIUM),
    HARD(CLUE_FRACTION_HARD, I18N_DIFFICULTY_HARD);

    private final double clueFraction;
    private final String i18nKey;

    /**
     * Initializes a difficulty with its clue density and localization key.
     * Allows each level to express both behavior and display name.
     */
    Difficulty(double clueFraction, String i18nKey) {
        this.clueFraction = clueFraction;
        this.i18nKey = i18nKey;
    }

    /**
     * Calculates how many values to remove from the board
     * based on the difficulty's clue fraction.
     */
    public int calculateValuesToRemove(int boardSize) {
        int totalCells = boardSize * boardSize;
        int cluesToKeep = (int) (clueFraction * totalCells);
        return totalCells - cluesToKeep;
    }

    public double getClueFraction() { return clueFraction; }

    /**
     * Converts {@code Difficulty} enum into a transport-safe DTO.
     */
    // TODO: Move Difficulty to game-layer in future
    public static DifficultyDto toDto(Difficulty difficulty) {
        return DifficultyDto.valueOf(difficulty.name());
    }

    /**
     * Parses a {@code DifficultyDto} back into its domain enum equivalent.
     */
    public static Difficulty fromDto(DifficultyDto dto) {
        return Difficulty.valueOf(dto.name());
    }


    @Override
    public String toString() {
        return Localization.get(i18nKey);
    }
}
