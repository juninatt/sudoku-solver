package se.pbt.sudokusolver.models;

import se.pbt.sudokusolver.utils.Constants;
import se.pbt.sudokusolver.utils.Localization;

/**
 * Represents the difficulty levels of a Sudoku puzzle.
 * Each difficulty level determines how many clues (pre-filled numbers) will be provided in the puzzle.
 */
public enum Difficulty {
    EASY(0.50, Constants.UI.Texts.Difficulty.EASY),
    MEDIUM(0.35, Constants.UI.Texts.Difficulty.MEDIUM),
    HARD(0.20, Constants.UI.Texts.Difficulty.HARD);

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


    @Override
    public String toString() {
        return Localization.get(i18nKey);
    }
}
