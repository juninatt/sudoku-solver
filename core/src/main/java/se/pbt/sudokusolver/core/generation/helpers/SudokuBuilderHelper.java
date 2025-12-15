package se.pbt.sudokusolver.core.generation.helpers;

import se.pbt.sudokusolver.core.models.SudokuBoard;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

import static se.pbt.sudokusolver.core.constants.CoreConstants.EMPTY_CELL;


/**
 * Abstract superclass providing shared logic for Sudoku puzzle generation and validation.
 * Designed to be extended by classes such as {@link SolutionGenerator} and {@link UniquenessChecker},
 * which apply recursive techniques and rule enforcement to construct or assess Sudoku boards.
 */
public abstract class SudokuBuilderHelper {

    /**
     * Calculates the coordinates of the next cell to be processed.
     * If the current cell is at the end of a row, moves to the beginning of the next row.
     */
    protected final int[] getNextCellPos(int row, int col, int boardSize) {
        int nextRow = (col == boardSize - 1) ? row + 1 : row;
        int nextCol = (col == boardSize - 1) ? 0 : col + 1;
        return new int[]{nextRow, nextCol};
    }

    /**
     * Attempts to place each number from 1 to board size in the specified cell, unless it violates Sudoku rules.
     * For each valid placement, a custom strategy is executed.
     * The loop stops early if the strategy returns {@code true}.
     */
    protected final boolean tryNumbersInCell(SudokuBoard board, int row, int col, Function<Integer, Boolean> strategy) {
        int[] numbers = getShuffledNumbers(board.getRowLength());
        for (int num : numbers) {
            if (isPlacementAllowed(board, row, col, num)) {
                board.setValue(row, col, num);

                if (strategy.apply(num)) {
                    return true;
                }

                board.setValue(row, col, EMPTY_CELL); // Backtrack
            }
        }
        return false;
    }

    /**
     * Generates an array containing numbers from 1 to maxNumber in random order.
     * Used during backtracking to try possible values in a cell.
     */
    private int[] getShuffledNumbers(int maxNumber) {
        int[] numbers = new int[maxNumber];

        for (int i = 0; i < maxNumber; i++) {
            numbers[i] = i + 1;
        }

        // Fisher–Yates shuffle
        for (int i = numbers.length - 1; i > 0; i--) {
            int j = ThreadLocalRandom.current().nextInt(i + 1);
            int tmp = numbers[i];
            numbers[i] = numbers[j];
            numbers[j] = tmp;
        }

        return numbers;
    }


    /**
     * Checks whether a specific number can be placed in a given cell
     * without violating the standard Sudoku rules (row, column, subgrid).
     */
    private boolean isPlacementAllowed(SudokuBoard board, int row, int col, int num) {
        if (numberExistsInRowOrCol(board, row, col, num)) return false;

        // Calculate the starting coordinates of the subgrid that contains the cell (row, col)
        int[] subgrid = board.getSubgridSize();
        int subgridRows = subgrid[0];
        int subgridCols = subgrid[1];
        int startRow = row - row % subgridRows;
        int startCol = col - col % subgridCols;

        // Return false if the number already exists within this subgrid.
        for (int r = 0; r < subgridRows; r++) {
            for (int c = 0; c < subgridCols; c++) {
                if (board.getCellValue(startRow + r, startCol + c) == num) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Returns true if the number is found in either the specified row or column of the board.
     */
    private boolean numberExistsInRowOrCol(SudokuBoard board, int row, int col, int num) {
        int gridSize = board.getRowLength();
        for (int i = 0; i < gridSize; i++) {
            if (board.getCellValue(row, i) == num || board.getCellValue(i, col) == num) {
                return true;
            }
        }
        return false;
    }
}
