package se.pbt.sudokusolver.utils;

import se.pbt.sudokusolver.models.SudokuBoard;

/**
 * Utility class for determining whether a given {@link SudokuBoard} has exactly one valid solution.
 * Used during puzzle generation to ensure uniqueness of Sudoku puzzles.
 * <p>
 * This class performs a backtracking search and stops early if more than one solution is found.
 */
public class UniquenessChecker {

    /**
     * Checks whether the given Sudoku board has exactly one valid solution.
     * Makes a defensive copy of the board to avoid modifying the original.
     */
    public boolean hasUniqueSolution(SudokuBoard board) {
        return countSolutions(board.copy(), 0, 0, 0) == 1;
    }


    /**
     * Checks how many valid solutions exist for a given Sudoku board by attempting to solve it recursively.
     * It tries every valid number in each empty cell while respecting Sudoku rules, counting full solutions.
     * The method halts early if more than one solution is found, since only uniqueness is of interest.
     */
    private int countSolutions(SudokuBoard board, int row, int col, int solutionCount) {
        int boardSize = board.getSize();

        if (solutionCount > 1) { return solutionCount; }

        // Base case: if we've reached beyond the last row, a complete solution is found
        if (row == boardSize) { return solutionCount + 1; }

        int nextRow = (col == boardSize - 1) ? row + 1 : row;
        int nextCol = (col == boardSize - 1) ? 0 : col + 1;

        // Skip over cells that already contain a value from the original puzzle
        if (board.hasValueAt(row, col)) { return countSolutions(board, nextRow, nextCol, solutionCount); }

        // Try placing each possible number in the current cell
        for (int num = 1; num <= boardSize; num++) {
            if (SudokuValidator.isValidPlacement(board, row, col, num)) {
                board.setValue(row, col, num);

                // Proceed to the next cell to attempt completing the puzzle
                int updatedSolutionCount = countSolutions(board, nextRow, nextCol, solutionCount);

                // Restore the cell to its empty state before testing the next number
                board.setValue(row, col, 0);

                if (updatedSolutionCount > 1) { return updatedSolutionCount; }

                solutionCount = updatedSolutionCount;
            }
        }
        return solutionCount;
    }
}
