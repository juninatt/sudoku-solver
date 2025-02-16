package se.pbt.sudokusolver.models;

import se.pbt.sudokusolver.utils.Constants;

import java.util.Arrays;

/**
 * Represents the logical structure of a Sudoku board.
 * Manages cell values, enforces position validation, and triggers board validation upon completion.
 *
 * @see SudokuValidator
 */
public class SudokuBoard {
    private final int boardSize;
    private int filledCells;
    private final int[][] board;
    private final SudokuValidator validator;

    /**
     * Initializes an empty Sudoku board of the specified size.
     *
     * @param boardSize   The size of the board (e.g., 9 for a 9x9 grid).
     * @param validator   The {@link SudokuValidator} responsible for validating board completion.
     */
    public SudokuBoard(int boardSize, SudokuValidator validator) {
        this.boardSize = boardSize;
        filledCells = 0;
        this.board = new int[boardSize][boardSize];
        this.validator = validator;
    }

    /**
     * Sets a value at the specified position in the Sudoku grid.
     * If all cells are filled, the board is validated.
     */
    public void setValue(int row, int col, int value) {
        if (!positionIsValid(row, col)) {
            throw new IllegalArgumentException(Constants.ErrorMessages.INVALID_POSITION);
        }
        board[row][col] = value;
        filledCells++;
        if (filledCells == boardSize * boardSize) {
            validator.validateBoard(this);
        }
    }

    /**
     * Checks if a given row and column position is within the valid board range.
     */
    private boolean positionIsValid(int row, int col) {
        return row >= 0 && row < boardSize && col >= 0 && col < boardSize;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public int getValueAt(int row, int col) {
        return board[row][col];
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] row : board) {
            sb.append(Arrays.toString(row)).append("\n");
        }
        return sb.toString();
    }
}
