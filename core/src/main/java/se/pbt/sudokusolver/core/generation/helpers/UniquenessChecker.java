package se.pbt.sudokusolver.core.generation.helpers;

import se.pbt.sudokusolver.core.models.SudokuBoard;

import java.util.concurrent.atomic.AtomicInteger;

import static se.pbt.sudokusolver.core.constants.CoreConstants.EMPTY_CELL;


/**
 * Core component used during puzzle generation to verify that a {@link SudokuBoard} has exactly one solution.
 * This class uses recursive backtracking to count all valid solutions for a given board,
 * terminating early if more than one solution is found.
 */
public final class UniquenessChecker extends SudokuBuilderHelper {

    /**
     * Checks whether the given Sudoku {@link SudokuBoard} has exactly one valid solution.
     * Makes a defensive copy of the board to avoid modifying the original.
     */
    public boolean hasUniqueSolution(SudokuBoard board) {
        return isSolutionUnique(board.deepCopy(), 0, 0, new AtomicInteger(0));
    }

    /**
     * Solves the {@link SudokuBoard} recursively while counting the number of valid solutions.
     * Used to verify whether the board has exactly one unique solution.
     * Returns {@code true} if only one solution is found; otherwise, stops early and returns {@code false}.
     */
    private boolean isSolutionUnique(SudokuBoard board, int row, int col, AtomicInteger solutionCount) {
        int rowLength = board.getRowLength();
        int count = solutionCount.get();

        if (count > 1) return false;

        if (row >= rowLength) {
            solutionCount.incrementAndGet();
            return count == 1;
        }

        int[] nextCell = getNextCellPos(row, col, rowLength);
        int nextRow = nextCell[0];
        int nextCol = nextCell[1];

        if (board.getCellValue(row, col) == EMPTY_CELL) {
            return isSolutionUnique(board, nextRow, nextCol, solutionCount);
        }

        return !tryNumbersInCell(board, row, col, num -> {
            isSolutionUnique(board, nextRow, nextCol, solutionCount);
            return false;
        });
    }
}
