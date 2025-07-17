package se.pbt.sudokusolver.models;

import se.pbt.sudokusolver.utils.Localization;

import static se.pbt.sudokusolver.utils.Constants.UIConstants.*;

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
     * Constructs a difficulty level with a specified clue fraction and localization key.
     *
     * @param clueFraction The fraction of the board to be pre-filled with clues.
     * @param i18nKey      The key used for localization of the difficulty name based on the current language setting.
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


    @Override
    public String toString() {
        return Localization.get(i18nKey);
    }
}
