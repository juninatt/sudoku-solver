package se.pbt.sudokusolver.models;

import se.pbt.sudokusolver.utils.Constants;
import se.pbt.sudokusolver.utils.SudokuValidator;
import se.pbt.sudokusolver.viewmodels.SudokuViewModel;

import java.util.Arrays;

import static se.pbt.sudokusolver.utils.Constants.GameConstants.*;

/**
 * Represents the internal structure of a Sudoku board.
 * Stores and manages cell values, tracks progress, and provides access to board dimensions.
 * <p>
 * Note: This class does not enforce Sudoku rules — validation logic is handled externally
 * via {@link SudokuValidator}.
 */
public class SudokuBoard {
    private final int size;
    private final int[] subgridDimensions;
    private int filledCells;
    private final int[][] board;

    // Constructors

    /**
     * Initializes an empty Sudoku board of the specified size.
     * All cells are set to zero (empty). Subgrid dimensions are calculated upon construction.
     *
     * @param size the total size of the board (e.g., 9 for a 9x9 Sudoku)
     */
    public SudokuBoard(int size) {
        this.size = size;
        this.subgridDimensions = calculateSubgridSize();
        this.board = new int[size][size];
    }

    /**
     * Private constructor for deep copying an existing SudokuBoard.
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
     */
    public SudokuBoard copy() {
        return new SudokuBoard(this);
    }

    // Mutators

    /**
     * Places a value in the specified cell and tracks the number of filled cells.
     * This method is intended for use during the board construction phase only.
     * During gameplay, all value updates must go through the {@link SudokuViewModel},
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
     * Returns the total size of the board (e.g., 9 for a 9x9 Sudoku).
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns the dimensions of a subgrid (e.g., {3, 3} for a 9x9 board).
     */
    public int[] getSubgridDimensions() {
        return subgridDimensions;
    }

    // State Checkers

    /**
     * Checks if a specific cell is currently filled (non-zero).
     */
    public boolean hasValueAt(int row, int col) {
        return getValueAt(row, col) != EMPTY_CELL;
    }

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
