package se.pbt.sudokusolver.core.generation.helpers;

import se.pbt.sudokusolver.core.models.SudokuBoard;

import java.util.concurrent.atomic.AtomicInteger;

import static se.pbt.sudokusolver.generation.constants.GenerationConstants.*;


/**
 * Core component used during puzzle generation to verify that a {@link SudokuBoard} has exactly one solution.
 * This class uses recursive backtracking to count all valid solutions for a given board,
 * terminating early if more than one solution is found.
 */
public class UniquenessChecker extends SudokuBuilderHelper {

    /**
     * Checks whether the given Sudoku {@link SudokuBoard} has exactly one valid solution.
     * Makes a defensive copy of the board to avoid modifying the original.
     */
    public boolean hasUniqueSolution(SudokuBoard board) {
        return isSolutionUnique(board.copy(), FIRST_ROW_INDEX, FIRST_COLUMN_INDEX, new AtomicInteger(0));
    }

    /**
     * Solves the {@link SudokuBoard} recursively while counting the number of valid solutions.
     * Used to verify whether the board has exactly one unique solution.
     * Returns {@code true} if only one solution is found; otherwise, stops early and returns {@code false}.
     */
    private boolean isSolutionUnique(SudokuBoard board, int row, int col, AtomicInteger solutionCount) {
        if (solutionCount.get() > 1) return false;

        if (isBoardFull(row, board.getSize())) {
            solutionCount.incrementAndGet();
            return solutionCount.get() == 1;
        }

        int[] next = getNextCell(row, col, board.getSize());
        int nextRow = next[0];
        int nextCol = next[1];

        if (board.getValueAt(row, col) == EMPTY_CELL) {
            return isSolutionUnique(board, nextRow, nextCol, solutionCount);
        }

        return !tryNumbersInCell(board, row, col, num -> {
            isSolutionUnique(board, nextRow, nextCol, solutionCount);
            return solutionCount.get() > 1;
        });
    }
}
