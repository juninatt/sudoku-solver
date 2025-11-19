package se.pbt.sudokusolver.core.models;

import se.pbt.sudokusolver.core.validation.SudokuValidator;
import se.pbt.sudokusolver.shared.constants.Constants;

import java.util.Arrays;

import static se.pbt.sudokusolver.shared.constants.Constants.GameConstants.*;

/**
 * Holds the internal state of a Sudoku board.
 * Provides a structured representation of cell values and supports logic that depends on board size.
 * <p>
 * Validation logic is handled externally via {@link SudokuValidator}.
 */
public class SudokuBoard {
    private final int size;
    private final int[] subgridDimensions;
    private int filledCells;
    private final int[][] board;

    // Constructors

    /**
     * Creates an empty board of the chosen size.
     * Establishes its subgrid layout so later logic can rely on consistent dimensions.
     */
    public SudokuBoard(int size) {
        this.size = size;
        this.subgridDimensions = calculateSubgridSize();
        this.board = new int[size][size];
    }

    /**
     * Private constructor used to create a deep copy of an existing SudokuBoard.
     * Produces a fully independent instance by cloning all mutable fields.
     */
    private SudokuBoard(SudokuBoard original) {
        this.size = original.size;
        this.subgridDimensions = original.subgridDimensions.clone();
        this.filledCells = original.filledCells;

        this.board = new int[size][size];
        for (int row = 0; row < size; row++) {
            this.board[row] = original.board[row].clone();
        }
    }

    // Factory Methods

    /**
     * Creates a deep copy of this board.
     * Delegates to the private copy-constructor to ensure full structural independence.
     */
    public SudokuBoard copy() {
        return new SudokuBoard(this);
    }

    // Mutators

    /**
     * Updates a cell and adjusts the internal count of filled positions.
     * This method is intended for use during the board construction phase only.
     * During gameplay, all value updates must go through the {@code SudokuViewModel} class,
     * which internally delegates to this method.
     */
    public void setValue(int row, int col, int value) {
        int oldValue = board[row][col];
        board[row][col] = value;
        if (oldValue == EMPTY_CELL && value != EMPTY_CELL) {
            filledCells++;
        } else if (oldValue != EMPTY_CELL && value == EMPTY_CELL) {
            filledCells--;
        }
    }

    // Accessors

    /**
     * Retrieves the value stored in a specific cell on the board.
     */
    public int getValueAt(int row, int col) {
        return board[row][col];
    }

    /**
     * Get the overall width/height of the board so other logic can adapt to its size.
     */
    public int getSize() {
        return size;
    }

    /**
     * Provides the row/column structure of each subgrid, used by validation and generation routines.
     */
    public int[] getSubgridDimensions() {
        return subgridDimensions;
    }

    // State Checkers

    /**
     * Determines whether the board is completely filled.
     */
    public boolean isBoardFull() {
        return filledCells == size * size;
    }

    // Private Helpers

    /**
     * Calculates subgrid dimensions based on the board size.
     */
    private int[] calculateSubgridSize() {
        if (!SUPPORTED_BOARD_SIZES.contains(size)) {
            throw new IllegalArgumentException(
                    String.format(ERROR_INVALID_BOARD_SIZE, size)
            );
        }
        return Constants.GameConstants.getBlockLayout(size);
    }

    // Overrides

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] row : board) {
            sb.append(Arrays.toString(row)).append("\n");
        }
        return sb.toString();
    }
}
