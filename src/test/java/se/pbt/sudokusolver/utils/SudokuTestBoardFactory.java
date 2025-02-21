package se.pbt.sudokusolver.utils;

import se.pbt.sudokusolver.models.SudokuBoard;

/**
 * Utility class for setting up test boards with predefined values.
 * Provides helper methods to populate {@link SudokuBoard} instances for validation testing.
 */
public class SudokuTestBoardFactory {

    private SudokuTestBoardFactory() {
        // Prevent instantiation, as this is a utility class
    }

    /**
     * Populates a Sudoku board with predefined values.
     *
     * @param board  The {@link SudokuBoard} to populate.
     * @param values A 2D integer array representing the board state.
     */
    public static void fillBoard(SudokuBoard board, int[][] values) {
        for (int row = 0; row < values.length; row++) {
            for (int col = 0; col < values[row].length; col++) {
                board.setValue(row, col, values[row][col]);
            }
        }
    }
}
