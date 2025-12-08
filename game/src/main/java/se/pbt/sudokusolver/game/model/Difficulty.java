package se.pbt.sudokusolver.game.model;

import se.pbt.sudokusolver.shared.game.PuzzleDifficulty;
import se.pbt.sudokusolver.shared.localization.Localization;

import static se.pbt.sudokusolver.core.constants.CoreConstants.*;

/**
 * Represents the difficulty levels of a Sudoku puzzle.
 * Each difficulty level determines how many clues (pre-filled numbers) will be provided in the puzzle.
 */
public enum Difficulty implements PuzzleDifficulty {
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


    @Override
    public double getClueFraction() {
        return clueFraction;
    }

    @Override
    public String toString() {
        return Localization.get(i18nKey);
    }
}
