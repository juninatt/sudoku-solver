package se.pbt.sudokusolver.builders.helpers;

import se.pbt.sudokusolver.models.SudokuBoard;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Abstract superclass providing shared logic for Sudoku puzzle generation and validation.
 * Designed to be extended by classes such as {@link SolutionGenerator} and {@link UniquenessChecker},
 * which apply recursive techniques and rule enforcement to construct or assess Sudoku boards.
 */
public abstract class SudokuBuilderHelper {

    /**
     * Determines whether the current row index indicates that the board is completely filled.
     */
    protected boolean isBoardFull(int row, int boardSize) {
        return row >= boardSize;
    }

    /**
     * Calculates the coordinates of the next cell to be processed.
     * If the current cell is at the end of a row, moves to the beginning of the next row.
     */
    protected final int[] getNextCell(int row, int col, int boardSize) {
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
        for (int num : getShuffledNumbers(board.getSize())) {
            if (isPlacementAllowed(board, row, col, num)) {
                board.setValue(row, col, num);

                if (strategy.apply(num)) {
                    return true;
                }

                board.setValue(row, col, 0); // Backtrack
            }
        }
        return false;
    }

    /**
     * Generates a list of integers from 1 to the board size in random order.
     * Used during backtracking to try possible numbers in a cell.
     */
    private List<Integer> getShuffledNumbers(int boardSize) {
        List<Integer> numbers = IntStream.rangeClosed(1, boardSize)
                .boxed()
                .collect(Collectors.toList());
        Collections.shuffle(numbers);
        return numbers;
    }

    /**
     * Checks whether a specific number can be placed in a given cell
     * without violating the standard Sudoku rules (row, column, subgrid).
     */
    private boolean isPlacementAllowed(SudokuBoard board, int row, int col, int num) {
        // Return false if the number already exists in the same row or column
        for (int i = 0; i < board.getSize(); i++) {
            if (board.getValueAt(row, i) == num || board.getValueAt(i, col) == num) {
                return false;
            }
        }

        // Calculate the starting coordinates of the subgrid that contains the cell (row, col)
        int[] subgrid = board.getSubgridDimensions();
        int subgridRows = subgrid[0];
        int subgridCols = subgrid[1];
        int startRow = row - row % subgridRows;
        int startCol = col - col % subgridCols;

        // Return false if the number already exists within this subgrid.
        for (int r = 0; r < subgridRows; r++) {
            for (int c = 0; c < subgridCols; c++) {
                if (board.getValueAt(startRow + r, startCol + c) == num) {
                    return false;
                }
            }
        }

        return true;
    }
}
